package cn.hegongda.service.article;


import cn.hegongda.pojo.TNotice;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

public interface ArticleManagerService {

    // 获取待审核的文章
    PageResult getCheckArticles(QueryPageBean queryPageBean);

    // 文章审核通过
    Result approved(Integer id);

    // 自动审核文章
    Result autoCheckArticle();

    // 手动审核
    Result handCheckArticle();

    // 审核不通过
    Result failPub(TNotice notice);
}
