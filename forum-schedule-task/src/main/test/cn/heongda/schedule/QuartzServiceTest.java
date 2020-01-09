package cn.heongda.schedule;


import cn.hegongda.service.QuartzService;
import com.sun.corba.se.impl.orb.ParserTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springmvc.xml")
public class QuartzServiceTest {


    @Autowired
    private QuartzService quartzService;

    @Test
    public void test1() throws InterruptedException {
         quartzService.startTask("read","read","10,20 * * ? * *",()->{
             System.out.println("读书时间到了");
         });
         Thread.sleep(1000000000);
    }


}
