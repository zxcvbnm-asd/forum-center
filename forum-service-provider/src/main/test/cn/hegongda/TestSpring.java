package cn.hegongda;

import cn.hegongda.mapper.FanAttenMapper;
import cn.hegongda.mapper.TArticleMapper;
import cn.hegongda.pojo.TArticle;
import cn.hegongda.result.Result;
import cn.hegongda.service.ArticleServiceImpl;
import cn.hegongda.service.FanAttenService;
import cn.hegongda.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-dao.xml","classpath:spring-service.xml"
        ,"classpath:spring-tx.xml","classpath:spring-redis.xml"})
public class TestSpring {

    @Autowired
    private TArticleMapper articleMapper;

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private JedisPool jedisPool;


    @Autowired
    private FanAttenService fanAttenService;

    @Test
    public void test1(){
       Map<String,String> map = new HashMap();
       map.put("uid",12+"");
       map.put("beginTime","2019-01");
       map.put("endTime","2019-12");
        List<Integer> preMonths = articleMapper.getPreMonths(map);
        for (Integer preMonth : preMonths) {
            System.out.println(preMonth);
        }
    }

    @Test
    public void testMaxnum(){
        Result supportNum = articleService.getSupportNum(33);
        System.out.println(supportNum.getData());
    }

    @Test
    public void testFan(){
        Result articleList = fanAttenService.getArticleList(12);
        List<Map> data = (List<Map>) articleList.getData();
        System.out.println(data.size());
    }
}
