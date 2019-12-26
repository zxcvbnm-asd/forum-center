package cn.hegongda.service;

import cn.hegongda.constant.RedisConstant;
import cn.hegongda.mapper.TArticleCategoryMapper;
import cn.hegongda.mapper.TArticleMapper;
import cn.hegongda.pojo.TArticle;
import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.utils.JsonUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service(interfaceClass = ArticleService.class)
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private TArticleMapper articleMapper;

    @Autowired
    private TArticleCategoryMapper articleCategoryMapper;

    @Autowired
    private JedisPool jedisPool;

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);


    // 查询出所有的一级分类
    @Override
    public List<TArticleCategory> findFirstCategory() {
        List<TArticleCategory> list = articleCategoryMapper.findAll();
        return list;
    }

    // 根据以及分类查询子类
    @Override
    public List<TArticleCategory>findSecondCategory(Integer parentId) {

        List<TArticleCategory> list = articleCategoryMapper.findChildByParentId(parentId);
        return list;
    }

    // 发布文章让其成为审核状态
    @Override
    @Transactional
    public Result pubArticle(TArticle article) {
        // 先从数据库中查询是否存在改文章，存在只需更改其状态即可，不存在直接插入
        Integer status = article.getStatus();
        int number = 0;
        if (status == 5){
            // 定时发布的文章，只需要将数据库中状态修改为1即可
            article.setStatus(1);
            number = articleMapper.updateByPrimaryKey(article);
        }else if(status == 3){
            // 存草稿中的文章重新发送
            article.setStatus(1);
            number = articleMapper.updateByPrimaryKey(article);

        }  else {
            // 没有定时发布只需要直接插入数据库中即可
             number = articleMapper.insert(article);
        }
        if (number > 0){
            // 将待审核的文章放入到redis中，以便于管理员从中获取审核
            Jedis jedis = jedisPool.getResource();
            jedis.rpush(RedisConstant.CHECK_ARTICLE, JsonUtils.objectToJson(article));
            return new Result(true, "审核通过后即可发布");
        }
        return new Result(false, "发布失败",article);
    }

    // 将文章保存到草稿箱
    @Override
    @Transactional
    public Result saveDraft(TArticle article) {
        if(article == null){
            return new Result(false, "请按照正常方式操作");
        }
        // 设置相关参数
        Date pubTime = new Date();
        article.setPubTime(pubTime);
        article.setStatus(3);  // 存入草稿箱
        int number = articleMapper.insert(article);
        if (number > 0){
            return new Result(true, "成功存入草稿箱");
        }
        return new Result(false, "存入失败");
    }



    // 定时发布文章
    @Override
    @Transactional
    public Result schedulePub(TArticle article) {
        if(article == null){
            return new Result(false, "请认真填写文章相关信息");
        }
        Date pubTime = article.getPubTime();
        long time = pubTime.getTime() - new Date().getTime();
        if (pubTime.getTime() - new Date().getTime() < 0){
            return new Result(false,"定时发布时间必须大于当前系统时间");
        }
        // 将文章设置为定时状态,并保存到数据库中
        article.setStatus(5);
        articleMapper.add(article);

        // 定时发布
        schedulePub(new SchedulePubAtricleTask(article),time);

        return new Result(true,"操作成功");
    }

    // 分页查询全部文章
    @Override
    public PageResult findAllByPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<Map> list = articleMapper.findAllByPage(queryPageBean.getQueryString());
        PageInfo info = new PageInfo(list);
        return new PageResult(info.getTotal(),list,"查询成功",true);
    }

    // 定时发布方法
    private void schedulePub(Callable<Result> task, long time){
        /**
         * 待解决返回参数校验问题
         */
        ScheduledFuture<Result> future = scheduledExecutorService.schedule(task, time, TimeUnit.MILLISECONDS);
    }

    // 定时发布文章的runnable类
    private class SchedulePubAtricleTask implements Callable<Result>{

        private TArticle article;

        public SchedulePubAtricleTask(TArticle article) {
            this.article = article;
        }

        @Override
        public Result call() throws Exception {
            // 先在数据库中找出是否还存在该定时文章，存在，说明用户并没删除定时发布的文章
            TArticle tArticle = articleMapper.selectByPrimaryKey(article.getId());
            if (tArticle == null){
                return null;
            }
            // 存在，将文章在指定时间发布，进入审核状态
            return pubArticle(this.article);
        }
    }


    // 根据id查询文章
    @Override
    public Result findById(Integer id) {
        if (id == null){
            return new Result(false, "传入参数有误");
        }
        TArticle article =  articleMapper.selectByPrimaryKey(id);
        return new Result(true,"成功",article);
    }

    // 根据id删除文章
    @Override
    @Transactional
    public Result deleteById(Integer id) {
        if (id == null){
            return new Result(false, "传入参数有误");
        }

        int number = articleMapper.deleteByPrimaryKey(id);
        if (number > 0){
            return new Result(true, "删除成功");
        }
        return new Result(false, "删除失败");
    }


    /*
     * 查询每种分类阅读量最多的几篇文章
     */
    @Override
    public Result findMaxNumArticle() {
        List<List<TArticle>> list = new ArrayList<>();
        // 先从redis中获取
        Jedis jedis = jedisPool.getResource();
        List<String> jsonList = jedis.lrange(RedisConstant.MAX_NUM_ARTICLE_LIST, 0, -1);
        if (jsonList != null && jsonList.size() > 0){
            for (String json : jsonList) {
                List<TArticle> articles = JsonUtils.jsonToList(json, TArticle.class);
                list.add(articles);
            }
            return new Result(true,"操作成功",list);
        }
        // 查询出一级分类
        List<TArticleCategory> firstCategory = findFirstCategory();
        // 根据一级分类的id查询文章
        for (TArticleCategory tArticleCategory : firstCategory) {
            List<TArticle> articles = articleMapper.findMaxNumArticle(tArticleCategory.getId());
            // 为了增强性能，将查询结果放到redis中
            jedis.rpush(RedisConstant.MAX_NUM_ARTICLE_LIST,JsonUtils.objectToJson(articles));
            list.add(articles);
        }
        return new Result(true,"操作成功",list);
    }
}
