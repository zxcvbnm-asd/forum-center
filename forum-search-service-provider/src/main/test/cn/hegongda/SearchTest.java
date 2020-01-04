package cn.hegongda;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springmvc.xml")
public class SearchTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient ;


    @Test
    public void testElasticSearch() throws IOException {
        // 设置索引库
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("book");

        // 设置分片信息
        createIndexRequest.settings(Settings.builder().put("number_of_shards",1)
              .put("number_of_replicas", 0)) ;

        // 设置映射信息 （粗查询 ： ik_smart  细查询 ： ik_max_word）
        createIndexRequest.mapping("doc"," {\n" + " \t\"properties\": {\n" + " \"name\": {\n" + " \"type\": \"text\",\n" + " \"analyzer\":\"ik_max_word\",\n" + " \"search_analyzer\":\"ik_smart\"\n" + " },\n" + " \"description\": {\n" + " \"type\": \"text\",\n" + " \"analyzer\":\"ik_max_word\",\n" + " \"search_analyzer\":\"ik_smart\"\n" + " },\n" + " \"studymodel\": {\n" + " \"type\": \"keyword\"\n" + " },\n" + " \"price\": {\n" + " \"type\": \"float\"\n" + " }\n" + " }\n" + "}", XContentType.JSON);

        // 创建索引操作客户端
        IndicesClient indices = restHighLevelClient.indices();

        // 创建响应对象
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);

        // 得到相应结果
        boolean acknowledged = createIndexResponse.isShardsAcknowledged();

        System.out.println(acknowledged);
    }


    // 删除索引库
    @Test
    public void test1() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("book");

        IndicesClient indices = restHighLevelClient.indices();
        DeleteIndexResponse response = indices.delete(deleteIndexRequest);
        System.out.println(response.isAcknowledged());
    }


    @Test
    public void testInsert() throws IOException {

        Map<String , Object> map = new HashMap<>();

        map.put("name", "css 实战");
        map.put("description", "本课程主要从四个章节进行讲解： 1.html入门 2.html到放弃");
        map.put("studymodel", "201001");
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy/MM/dd");
        map.put("timestamp",dateFormat.format(new Date()));
        map.put("price", 5.6f) ;

        IndexRequest indexRequest = new IndexRequest("user" ,"doc");
        indexRequest.source(map);
        IndexResponse response = restHighLevelClient.index(indexRequest);
        DocWriteResponse.Result result = response.getResult();
        System.out.println(result);
    }

    @Test
    public void testSelect() throws IOException {
        GetRequest getRequest = new GetRequest("user" , "doc", "50J_b28BzHyu6U_-N-nG");
        GetResponse response = restHighLevelClient.get(getRequest);
        Map<String, Object> map = response.getSourceAsMap();
        System.out.println(map);
    }

    @Test
    public void testUpdate() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("user" , "doc" ,"50J_b28BzHyu6U_-N-nG") ;
        Map<String,String> map = new HashMap<>();
        map.put("name" , "Springboot");
        updateRequest.doc(map) ;
        UpdateResponse response = restHighLevelClient.update(updateRequest);
        GetResult result = response.getGetResult();
        RestStatus status = response.status();
        System.out.println(status);
    }

    @Test
    public void testDeleteById() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("user" , "doc" ,"50J_b28BzHyu6U_-N-nG") ;
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest);
        RestStatus status = delete.status();
        System.out.println(status);
    }

    @Test
    public void testQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("item") ;
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});
        // 进行分页查询， 从第0页开始查询
        searchSourceBuilder.from(1);
        searchSourceBuilder.size(1);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest);
        SearchHits hits = response.getHits();
        show(hits);
    }

    // termQuery 是根据词语进行精确查询
    @Test
    public void testTermQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("item") ;
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
       // searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));
        // 根据id进行进行精确匹配

        String[] split = new String[]{"1","2"};
        List<String> idList = Arrays.asList(split);
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", idList));
        searchSourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});


        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest);
        SearchHits hits = response.getHits();
        show(hits);
    }

    // match Query 先对查询的的词语进行分词，然后在在匹配到文章中进行匹配
    // matchQuery().operator(Operator.And || OR) // 例如 spring开发 会分词为spring与开发， OR只要有一个词匹配的话就返回
    // minimumShouldMatch 分成的词有80%匹配即可， 如分成3 个词，则匹配2.4个即可，取2个
    @Test
    public void testMatchQuery() throws IOException {
       SearchRequest searchRequest = new SearchRequest("item");
       searchRequest.types("doc");

       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
     //  searchSourceBuilder.query(QueryBuilders.matchQuery("name","spring开发").operator(Operator.AND));
        searchSourceBuilder.query(QueryBuilders.matchQuery("description","java开发框架").minimumShouldMatch("80%"));
       searchSourceBuilder.fetchSource(new String[]{"name","description"}, new String[]{});

       searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);

        SearchHits hits = response.getHits();
        show(hits);

    }

    // 一次匹配多个字段
    @Test
    public void testMultiQuery() throws IOException {
       SearchRequest searchRequest = new SearchRequest("item");
       searchRequest.types("doc");
       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

       // 匹配name description属性
       MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description").minimumShouldMatch("50%");
       multiMatchQueryBuilder.field("name",10);  // 将name的权重提升十倍
       searchSourceBuilder.query(multiMatchQueryBuilder);
       searchSourceBuilder.fetchSource(new String[]{"name","description"}, new String[]{});

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);

        SearchHits hits = response.getHits();
        show(hits);
    }

    // 一次匹配多个字段
    @Test
    public void testBoolQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("item");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 第一种查询
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description").minimumShouldMatch("50%");
        multiMatchQueryBuilder.field("name",10);  // 将name的权重提升十倍

        searchSourceBuilder.fetchSource(new String[]{"name","description"}, new String[]{});
        // 第二种查询
        TermQueryBuilder termQuery = QueryBuilders.termQuery("studymodel", 201002);

        // boolean查询， 判断前两种查询是否满足（must 必须  should : 应该  not must : 不必须）
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
        booleanQueryBuilder.must(multiMatchQueryBuilder);
        booleanQueryBuilder.must(termQuery);

        // 对boolean 查询进行封装即可
        searchSourceBuilder.query(booleanQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);

        SearchHits hits = response.getHits();
        show(hits);

    }

    // 布尔查询外加过滤器
    @Test
    public void testBoolQueryAndFilter() throws IOException {
        SearchRequest searchRequest = new SearchRequest("item");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 第一种查询
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description").minimumShouldMatch("50%");
        multiMatchQueryBuilder.field("name",10);  // 将name的权重提升十倍

        searchSourceBuilder.fetchSource(new String[]{"name","description"}, new String[]{});

        searchSourceBuilder.query(multiMatchQueryBuilder);

        // boolean查询， 判断前两种查询是否满足（must 必须  should : 应该  not must : 不必须）
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
        booleanQueryBuilder.must(searchSourceBuilder.query());

        booleanQueryBuilder.filter(QueryBuilders.termQuery("studymodel",201001));
        booleanQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        // 对boolean 查询进行封装即可
        searchSourceBuilder.query(booleanQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);

        SearchHits hits = response.getHits();
        show(hits);

    }


    // 进行排序 可以对一个字段或者多个字段进行排序， float ， keyword， date , 不能对text进行排序
    @Test
    public void testOrder() throws IOException {
        SearchRequest searchRequest = new SearchRequest("item");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 第一种查询
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("基础", "name", "description").minimumShouldMatch("50%");
        multiMatchQueryBuilder.field("name",10);  // 将name的权重提升十倍

        searchSourceBuilder.fetchSource(new String[]{"name","description"}, new String[]{});

        searchSourceBuilder.query(multiMatchQueryBuilder);

        // boolean查询， 判断前两种查询是否满足（must 必须  should : 应该  not must : 不必须）
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
        booleanQueryBuilder.must(searchSourceBuilder.query());

        booleanQueryBuilder.filter(QueryBuilders.termQuery("studymodel",201001));
        booleanQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        // 对boolean 查询进行封装即可
        searchSourceBuilder.query(booleanQueryBuilder);

        // 按照时间进行排序
        searchSourceBuilder.sort(new FieldSortBuilder("timestamp").order(SortOrder.ASC));

        // 设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));

        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);

        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            String index = hit.getIndex();
            String id = hit.getId();
            float score = hit.getScore();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Object name = sourceAsMap.get("name");

            // 取出高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null ){
                HighlightField highlightField = highlightFields.get("name");
                if (highlightField != null ){
                    Text[] fragments = highlightField.fragments();
                    StringBuffer buffer = new StringBuffer();
                    for (Text fragment : fragments) {
                        buffer.append(fragment);
                    }
                    name = buffer.toString();
                }
            }
            System.out.println(name);
            Object description = sourceAsMap.get("description");
            System.out.println(description);
            System.out.println("=======================");
        }

    }



    private void show(SearchHits hits){
        for (SearchHit hit : hits) {
            String index = hit.getIndex();
            String id = hit.getId();
            float score = hit.getScore();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Object name = sourceAsMap.get("name");
            System.out.println(name);
            Object description = sourceAsMap.get("description");
            System.out.println(description);
            System.out.println("=======================");
        }
    }
}
