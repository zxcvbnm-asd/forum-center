package cn.hegongda.search.controller;

import cn.hegongda.search.pojo.Student;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class ArticleSearchController {

    @Autowired
    private RestHighLevelClient restHighLevelClient ;

    @Autowired
    private RestClient restClient;

    @RequestMapping("/show.do")
    @ResponseBody
    public String show() throws IOException {
        // 设置索引库
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("article");

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

        return "success";
    }
}
