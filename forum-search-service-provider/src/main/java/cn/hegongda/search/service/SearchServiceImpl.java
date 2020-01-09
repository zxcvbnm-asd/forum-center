package cn.hegongda.search.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.pojo.ArticlePub;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.service.SearchService;
import com.alibaba.dubbo.config.annotation.Service;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SearchService.class)
public class SearchServiceImpl implements  SearchService{

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /*
     * 按照条件进行分页查询文章
     */
    @Override
    public PageResult findAtricleByQuery(QueryPageBean queryPageBean) throws IOException {
        if ( queryPageBean == null ){
            return new PageResult(MessageConstant.PARAM_NULL_MESSAGE, false);
        }

        SearchRequest searchRequest = new SearchRequest("forum_article");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (queryPageBean.getQueryString() == null) {
            searchSourceBuilder.query();
            // 按照num进行降序排列
            searchSourceBuilder.sort(new FieldSortBuilder("num").order(SortOrder.DESC));
        } else {
            // 多重匹配查询（title, username, labels, cname）
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(queryPageBean.getQueryString(), "title", "labels", "username", "cname");
            // 将title的权重提升10倍
            multiMatchQueryBuilder.field("title",10);
            searchSourceBuilder.query(multiMatchQueryBuilder);
            // 设置高亮字段
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<font class='eslight'>");
            highlightBuilder.postTags("</font>");
            highlightBuilder.fields().add(new HighlightBuilder.Field("title"));
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        // 分页查询
        int size = ( queryPageBean.getCurrentPage() - 1 )  * queryPageBean.getPageSize();
        searchSourceBuilder.from(size);
        searchSourceBuilder.size( queryPageBean.getPageSize() );
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);
        // 获取结果集
        SearchHits hits = response.getHits();
        // 总记录数
        long total = hits.getTotalHits();

        List<ArticlePub> articlePubs = getLightArticlePubs(hits);

        return new PageResult(total, articlePubs, MessageConstant.OPERATION_SUCCESS,true);
    }


    /*
     * 根据分类查询文章
     */
    @Override
    public PageResult findArticleByCid(Integer cid, QueryPageBean queryPageBean) throws IOException {
        if ( cid == null || queryPageBean == null ) {
            return new PageResult( MessageConstant.PARAM_NULL_MESSAGE, false);
        }
        SearchRequest searchRequest = new SearchRequest("forum_article");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder  = new SearchSourceBuilder();
        // 精确查询
        searchSourceBuilder.query(QueryBuilders.termQuery("cid", cid));
        // 分页
        searchSourceBuilder.from((queryPageBean.getCurrentPage() - 1) * queryPageBean.getPageSize() );
        searchSourceBuilder.size(queryPageBean.getPageSize());
        // 排序
        searchSourceBuilder.sort(new FieldSortBuilder("num").order(SortOrder.DESC));
        // 获取结果集，进行相关的处理
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits();

        List<ArticlePub> list = getArtcilePubs(hits);

        return new PageResult(total,list, MessageConstant.OPERATION_SUCCESS, true);
    }

    /*
     * 根据父分类进行文章查询
     */
    @Override
    public PageResult findArticleByParentId(Integer parentId, QueryPageBean queryPageBean) throws IOException {
        if (parentId == null || queryPageBean == null ){
            return new PageResult( MessageConstant.PARAM_NULL_MESSAGE, false);
        }
        SearchRequest searchRequest = new SearchRequest("forum_article");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("parent_id",parentId));

        int size = ( queryPageBean.getCurrentPage() - 1 ) * queryPageBean.getPageSize();
        searchSourceBuilder.from(size);
        searchSourceBuilder.size(queryPageBean.getPageSize());

        // 排序
        searchSourceBuilder.sort(new FieldSortBuilder("num").order(SortOrder.DESC));
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest);
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits();
        List<ArticlePub> list = getArtcilePubs(hits);

        return new PageResult(total, list, MessageConstant.OPERATION_SUCCESS, true );
    }


    /*
     * 根据cid 或者 parentid 外加条件进行查询
     */
    @Override
    public PageResult findArticleByIdAndQueryString(Integer cid, Integer parentId, QueryPageBean queryPageBean) throws IOException {
        if (cid == null || parentId == null || queryPageBean == null ){
            return new PageResult( MessageConstant.PARAM_NULL_MESSAGE, false);
        }

        SearchRequest searchRequest = new SearchRequest("forum_article");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 按照输入的条件进行匹配
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(queryPageBean.getQueryString(), "title", "cname", "labels", "username");
        multiMatchQueryBuilder.field("title", 10);
        searchSourceBuilder.query(multiMatchQueryBuilder);

        // 构造bool查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(searchSourceBuilder.query());

        // 进行过滤
        if (cid != -1) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("cid",cid));
        }
        if ( parentId != -1) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("parent_id", parentId));
        }
        searchSourceBuilder.query(boolQueryBuilder);

        // 设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("title"));
        searchSourceBuilder.highlighter(highlightBuilder);

        int size = ( queryPageBean.getCurrentPage() - 1 ) * queryPageBean.getPageSize();
        searchSourceBuilder.from(size);
        searchSourceBuilder.size(queryPageBean.getPageSize());

        // 获取结果集并进行处理
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits();
        List<ArticlePub> artcilePubs = getLightArticlePubs(hits);
        return new PageResult( total, artcilePubs, MessageConstant.OPERATION_SUCCESS, true);
    }




    private List<ArticlePub> getLightArticlePubs(SearchHits hits){
        List<ArticlePub> articlePubs = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String title = (String) sourceAsMap.get("title");
            // 取出高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null ) {
                HighlightField highlightField = highlightFields.get("title");
                if (highlightField != null) {
                    Text[] fragments = highlightField.fragments();
                    if (fragments != null && fragments.length > 0) {
                        StringBuffer buffer = new StringBuffer();
                        for (Text fragment : fragments) {
                            buffer.append(fragment);
                        }
                        title = buffer.toString();
                    }
                }
            }

            String cname = (String) sourceAsMap.get("cname");
            String labels = (String) sourceAsMap.get("labels");
            String username = (String) sourceAsMap.get("username");
            Integer num = (Integer) sourceAsMap.get("num");
            String pubTime = (String) sourceAsMap.get("pub_time");
            String coverUrl = (String) sourceAsMap.get("cover_url");
            Integer id = (Integer)sourceAsMap.get("id");
            Integer cid = (Integer)sourceAsMap.get("cid");
            Integer uid = (Integer)sourceAsMap.get("uid");

            ArticlePub articlePub = new ArticlePub();
            articlePub.setId(id);
            articlePub.setTitle(title);
            articlePub.setNum(num);
            articlePub.setCoverUrl(coverUrl);
            articlePub.setUsername(username);
            articlePub.setCname(cname);
            articlePub.setLabels(labels);
            articlePub.setPubTime(pubTime);
            articlePub.setCid(cid);
            articlePub.setUid(uid);

            articlePubs.add(articlePub);
        }

        return articlePubs;
    }

    private List<ArticlePub> getArtcilePubs(SearchHits hits){
        List<ArticlePub> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String title = (String) sourceAsMap.get("title");

            String cname = (String) sourceAsMap.get("cname");
            String labels = (String) sourceAsMap.get("labels");
            String username = (String) sourceAsMap.get("username");
            Integer num = (Integer) sourceAsMap.get("num");
            String pubTime = (String) sourceAsMap.get("pub_time");
            String coverUrl = (String) sourceAsMap.get("cover_url");
            Integer id = (Integer)sourceAsMap.get("id");
            Integer uid = (Integer)sourceAsMap.get("uid");
            Integer cid = (Integer)sourceAsMap.get("uid");

            ArticlePub articlePub = new ArticlePub();
            articlePub.setId(id);
            articlePub.setTitle(title);
            articlePub.setNum(num);
            articlePub.setCoverUrl(coverUrl);
            articlePub.setUsername(username);
            articlePub.setCname(cname);
            articlePub.setLabels(labels);
            articlePub.setPubTime(pubTime);
            articlePub.setUid(uid);
            articlePub.setCid(cid);

            list.add(articlePub);
        }

        return list;
    }
}
