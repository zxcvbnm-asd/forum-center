package cn.hegongda;

import cn.hegongda.constant.RedisConstant;
import cn.hegongda.mapper.*;
import cn.hegongda.pojo.*;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.*;
import cn.hegongda.service.article.ArticleManagerService;
import cn.hegongda.service.article.ArticleReportService;
import cn.hegongda.service.fan_atten.FanAttenService;
import cn.hegongda.service.report.ReportService;
import cn.hegongda.service.user.UserManagerService;
import cn.hegongda.utils.DateUtils;
import cn.hegongda.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.ParseException;
import java.util.*;

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

    @Autowired
    private CommentService commentService;

    @Autowired
    private AnnounceService announceService;

    @Autowired
    TArticleCategoryMapper tArticleCategoryMapper;

    @Autowired
    TUserMapper userMapper;

    @Autowired
    ArticleReportService articleReportService ;

    @Autowired
    private ArticleManagerService articleManagerService;

    @Autowired
    private ReportMapper reportMapper ;

    @Autowired
    private ReportService reportService;

    @Autowired
    private TImgMapper imgMapper;

    @Autowired
    private TImgCategoryMapper categoryMapper;




     @Test
     public void test(){
       TImgCategory imgCategory = new TImgCategory();
       imgCategory.setCname("北京图片");
       categoryMapper.insert(imgCategory);
     }


     @Test
     public void test05(){

     }
     // 构造被投诉文章的数据
     @Test
     public void testInsertinto(){
         List<TArticle> articles = articleMapper.findPublished();
         for (TArticle article : articles) {
             Report report = new Report();
             report.setCate(0);
             report.setCid(article.getId());
             report.setStatus(0);
             report.setUid(11);
             report.setType(2);
             report.setReportTime(new Date());
             report.setContent("辱骂他人");
             reportMapper.addReport(report);
         }
     }

     // 测试查询待审核文章
     @Test
     public void testCheck() throws InterruptedException {
        /* QueryPageBean queryPageBean = new QueryPageBean();
         queryPageBean.setPageSize(8);
         queryPageBean.setCurrentPage(1);
         queryPageBean.setQueryString("刘伟东");
         reportService.getReport(1, 0,queryPageBean);*/

        reportService.changeStatus(26);


     }

    // 向logstash的数据库表中添加数据
    @Test
    public void test2(){
        List<TArticle> published = articleMapper.findPublished();
        for (TArticle article : published) {
            ArticlePub articlePub = new ArticlePub();
            articlePub.setCid(article.getCid());
            TArticleCategory articleCategory = tArticleCategoryMapper.getCategoryNameById(article.getCid());
            articlePub.setCname(articleCategory.getCname());
            Integer parentId = articleCategory.getParentId();
            articlePub.setParentId(parentId);
            articlePub.setCoverUrl(article.getCoverUrl());
            articlePub.setLabels(article.getLabels());
            articlePub.setTitle(article.getTitle());
            articlePub.setNum(article.getNum());
            articlePub.setUid(article.getUid());
            Date pubTime = article.getPubTime();
            articlePub.setPubTime(DateUtils.format2(article.getPubTime()));
            TUser user = userMapper.selectByPrimaryKey(article.getUid());
            articlePub.setUsername(user.getUsername());
            articlePub.setId(article.getId());
            articlePub.setTimestamp(new Date());
            articleMapper.addArticlePub(articlePub);

        }
    }


    @Test
    public void testRedis(){
        Jedis jedis = jedisPool.getResource();
        List<TArticle> articles = articleMapper.findPublished();
        for (TArticle article : articles) {
            String json = JsonUtils.objectToJson(article);
            jedis.rpush(RedisConstant.CHECK_ARTICLE, json);
        }
    }


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

    @Test
    public void testAttenFan(){
        Result userAtten = fanAttenService.getUserFan(12);
        List<Map> list = (List<Map>) userAtten.getData();
        System.out.println("jaj");
    }

    @Test
    public void testComment(){
        CommentExpan comment = new CommentExpan();
        comment.setContent("镇海");
        comment.setCustomerId(12);
        comment.setContentId(23);
        commentService.pubComment(comment);
    }

    @Test
    public void testdeletComment(){
        Result result = commentService.deleteById(53L);
        System.out.println("ahah");
    }

    @Test
    public void testReport(){
        TCommentReport commentReport = new TCommentReport();
        commentReport.setStatus(1);
        commentReport.setReportTime(new Date());
        commentReport.setMark("打情骂俏");
        commentReport.setUid(11);
        commentReport.setCommentId(3L);

    }


    @Test
    public void testGetAttensByPage() throws ParseException {
        Date date = new Date();
        Date date1 = DateUtils.parseToDate("2019-12-21");
        QueryPageBean queryPageBean = new QueryPageBean();
        Date [] dates = {date,date1};
        queryPageBean.setTimeArray(dates);
        fanAttenService.getFansByTime(11,queryPageBean);
    }

    @Test
    public void testCommnetUser(){
        QueryPageBean queryPageBean = new QueryPageBean();
        queryPageBean.setPageSize(10);
        queryPageBean.setCurrentPage(1);
        commentService.getCommentsByPage(12,queryPageBean, 1);
    }


    @Test
    public void testAnnounce() {
         //announceService.changeStatus(11, 18);
       /* QueryPageBean queryPageBean = new QueryPageBean();
        queryPageBean.setPageSize(10);
        queryPageBean.setCurrentPage(1);
         announceService.getNotices(11,queryPageBean);*/

       announceService.changeNoticeStatus(1);

    }

    @Test
    public void testMap(){
       articleService.findById(403);
    }





}
