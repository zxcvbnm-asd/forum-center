package cn.hegongda.service.article;

import cn.hegongda.pojo.TArticle;
import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

import java.util.List;

public interface ArticleService {

    //查询文章一级分类
    public List<TArticleCategory> findFirstCategory();
    // 通过父id查询文章二级分类
    public List<TArticleCategory> findSecondCategory(Integer parentId);
    // 发布文章，向数据库中插入文章
    public Result pubArticle(TArticle article);
    // 文章存入草稿箱
    Result saveDraft(TArticle article);
    // 定时发布文章
    Result schedulePub(TArticle article);
    // 分页查询全部文章
    PageResult findAllByPage(QueryPageBean queryPageBean);
    // 根据id查找文章
    Result findById(Integer id);

    // 根据id删除文章
    Result deleteById(Integer id);
    // 查询每种分类阅读量最多的几篇文章
    Result findMaxNumArticle();
    // 查询点击量
    Result getSupportNum(Integer aid);
    // 增加点赞数
    Result addSupportNum(Integer aid, Integer number);
    // 将阅读数记录插入到数据库中
    Result readRecoderToDatabase(Integer total, String uid, String cid);
    // 新增分类
    Result addCategory(TArticleCategory category);



}
