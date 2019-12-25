package cn.hegongda;

import cn.hegongda.mapper.TArticleMapper;
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
@ContextConfiguration(locations = "classpath:spring-dao.xml")
public class TestSpring {

    @Autowired
    private TArticleMapper articleMapper;


    @Test
    public void test1(){
        List<Integer> lastWeek = articleMapper.getLastWeek();
        for (Integer integer : lastWeek) {
            System.out.println(integer);
        }

    }
}
