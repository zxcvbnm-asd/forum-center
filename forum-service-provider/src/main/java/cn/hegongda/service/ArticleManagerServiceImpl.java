package cn.hegongda.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.constant.RedisConstant;
import cn.hegongda.mapper.NoticeMapper;
import cn.hegongda.mapper.TArticleCategoryMapper;
import cn.hegongda.mapper.TArticleMapper;
import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.*;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.article.ArticleManagerService;
import cn.hegongda.task.TimerTask;
import cn.hegongda.utils.DateUtils;
import cn.hegongda.utils.JsonUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service(interfaceClass = ArticleManagerService.class)
public class ArticleManagerServiceImpl implements ArticleManagerService {

    @Autowired
    private TArticleMapper articleMapper;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private TArticleCategoryMapper articleCategoryMapper;

    @Autowired
    private TArticleCategoryMapper tArticleCategoryMapper;


    private ReentrantLock lock = new ReentrantLock();

    private TimerTask timerTask = new TimerTask();

    public ArticleManagerServiceImpl() {
    }

    /*
     * 后台获取待审核的文章
     */
    @Override
    public PageResult getCheckArticles(QueryPageBean queryPageBean) {
        if (queryPageBean == null ) {
            return new PageResult( MessageConstant.PARAM_NULL_MESSAGE, false);
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<ArticleExpan> list = articleMapper.getAritcleByStatus(1,queryPageBean.getQueryString());
        PageInfo info = new PageInfo(list);
        return new PageResult( info.getTotal(), list, MessageConstant.OPERATION_SUCCESS,true);
    }

    /*
     * 管理员审核文章通过
     */

    @Override
    @Transactional
    public Result approved(Integer id) {
        if (id == null ){
            return new Result( false, MessageConstant.PARAM_NULL_MESSAGE);
        }

        // 加锁，多个管理员操作时，可能导致多次插入数据库
        try {
            lock.lock();
            TArticle article = articleMapper.selectByPrimaryKey(id);
            if (article.getStatus() != 2) {
                article.setStatus(2);
                articleMapper.updateByPrimaryKey(article);

                // 将审核通过后的文章保存到t_article_pub中，用于增量控制
                ArticlePub articlePub = new ArticlePub();
                articlePub.setCid(article.getCid());
                articlePub.setUid(article.getUid());
                articlePub.setCoverUrl(article.getCoverUrl());
                articlePub.setLabels(article.getLabels());
                articlePub.setNum(article.getNum());
                articlePub.setTimestamp(new Date());
                articlePub.setTitle(article.getTitle());
                articlePub.setPubTime(DateUtils.format2(article.getPubTime()));
                articlePub.setId(article.getId());
                TUser user = userMapper.selectByPrimaryKey(article.getUid());
                articlePub.setUsername(user.getUsername());
                TArticleCategory category = articleCategoryMapper.getCategoryNameById(article.getCid());
                articlePub.setParentId(category.getParentId());
                articlePub.setCname(category.getCname());

                articleMapper.addArticlePub(articlePub);
            }
        } finally {
            lock.unlock();
        }
        return new Result(true,MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 自动审核文章
     */
    @Override
    public Result autoCheckArticle() {
        timerTask.execute(()->{
            System.out.println("执行");
            List<ArticleExpan> articleExpans = articleMapper.getAritcleByStatus(1, null);
            if (articleExpans != null || articleExpans.size() > 0) {
                for (ArticleExpan articleExpan : articleExpans) {
                    this.approved(articleExpan.getId());
                }
            }
        });
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 手动审核
     */
    @Override
    public Result handCheckArticle() {
        // 将自动审核关闭即可
        timerTask.shutDown();
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 文章审核不通过
     */

    @Override
    @Transactional
    public Result failPub(TNotice notice) {
        if (notice == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        notice.setStatus(0);
        notice.setTime(new Date());
        Integer number = noticeMapper.addNotice(notice);
        if ( number > 0) {
            return new Result(true, MessageConstant.OPERATION_SUCCESS );
        }
        return new Result( false, MessageConstant.OPERATION_FAIL );
    }


    /*
     * 文章下线，修改文章状态值
     */

    @Override
    @Transactional
    public Result offLineArticle(Integer id) {
        if (id == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }

        // 先查再改
        TArticle article = articleMapper.selectByPrimaryKey(id);
        if (article != null) {
            article.setStatus(4);
            articleMapper.updateByPrimaryKey(article);
            return new Result(true, MessageConstant.OPERATION_SUCCESS);
        }
        return new Result( false, MessageConstant.OPERATION_FAIL);
    }
}
