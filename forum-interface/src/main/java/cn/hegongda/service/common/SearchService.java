package cn.hegongda.service.common;

import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;

import java.io.IOException;

public interface SearchService {
    // 根据输入的条件进行查询文章
    PageResult findAtricleByQuery(QueryPageBean queryPageBean) throws IOException;

    // 根据分类进行查询文章
    PageResult findArticleByCid(Integer cid, QueryPageBean queryPageBean) throws IOException;

    // 根据父分类进行查询
    PageResult findArticleByParentId(Integer parentId, QueryPageBean queryPageBean) throws IOException;

    // 根据cid 或者 parentid 外加条件进行查询
    PageResult findArticleByIdAndQueryString(Integer cid, Integer parentId, QueryPageBean queryPageBean) throws IOException;
}
