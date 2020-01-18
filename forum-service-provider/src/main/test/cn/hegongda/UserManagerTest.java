package cn.hegongda;

import cn.hegongda.pojo.Rule;
import cn.hegongda.result.Result;
import cn.hegongda.service.user.UserManagerService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-dao.xml","classpath:spring-service.xml"
        ,"classpath:spring-tx.xml","classpath:spring-redis.xml"})
public class UserManagerTest {

    @Autowired
    private UserManagerService userManagerService;

    @Test
    public void test1(){
        Rule rule = new Rule();
        rule.setUid(11);
        rule.setStatus(1);
        Result result = userManagerService.addRule(rule);
    }
}
