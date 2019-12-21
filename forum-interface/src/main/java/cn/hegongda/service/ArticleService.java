package cn.hegongda.service;

import cn.hegongda.pojo.TArticle;
import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.result.Result;

import java.util.List;

public interface ArticleService {

    //查询文章一级分类
    public List<TArticleCategory> findFirstCategory();
    // 通过父id查询文章二级分类
    public List<TArticleCategory> findSecondCategory(Integer parentId);
    // 发布文章，向数据库中插入文章
    public Result pubArticle(TArticle article);
}
