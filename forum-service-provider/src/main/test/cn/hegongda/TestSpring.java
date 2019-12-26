package cn.hegongda;

import cn.hegongda.mapper.TArticleMapper;
import cn.hegongda.pojo.TArticle;
import cn.hegongda.service.ArticleServiceImpl;
import cn.hegongda.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        articleService.findMaxNumArticle();
    }
}
