package cn.hegongda.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  对elasticsearch的客户端进行相关的配置
 */
@Configuration
public class ElasticConfig {


    @Value("${elasticsearch.host}")
    private String host;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        HttpHost []httpHosts = new HttpHost[1];
        httpHosts[0] = new HttpHost(host.split(":")[0],Integer.parseInt(host.split(":")[1]));
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }

}
