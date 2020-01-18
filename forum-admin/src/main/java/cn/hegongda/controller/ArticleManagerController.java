package cn.hegongda.controller;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.pojo.TNotice;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.article.ArticleManagerService;

import cn.hegongda.service.article.ArticleReportService;
import cn.hegongda.service.article.ArticleService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/article-manager")
public class ArticleManagerController {

    @Reference
    private ArticleManagerService articleManagerService;


    @Reference
    private ArticleService articleService;

    @Reference
    private ArticleReportService articleReportService;

    @Autowired
    private HttpServletRequest request;



    /*
     * 获取待审核的文章
     */
    @RequestMapping("/getCheckArticles")
    @ResponseBody
    public PageResult getCheckArticles(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = articleManagerService.getCheckArticles(queryPageBean);
            return pageResult ;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult( MessageConstant.EXCEPTION_MESSAGE ,false);
        }
    }

    /*
     * 审核文章通过
     */
    @RequestMapping("/approved.do")
    @ResponseBody
    public Result approved(Integer id){
        try {
            Result result = articleManagerService.approved(id);
            return result;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result( false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 自动进行审核
     */
    @RequestMapping("/autoCheckArticle.do")
    @ResponseBody
    public Result autoCheckArticle(){
        try {
            Result result = articleManagerService.autoCheckArticle();
            return result;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result( false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 手动进行审核
     */
    @RequestMapping("/handCheckArticle.do")
    @ResponseBody
    public Result handCheckArticle(){
        try {
            Result result = articleManagerService.handCheckArticle();
            return result;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result( false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 根据id获取文章查看文章详情
     */
    @RequestMapping("/getArticleById.do")
    @ResponseBody
    public Result getArticleById(Integer id) {
        try {
            Result result = articleService.findById(id);
            return result ;
        } catch (Exception e ) {
            e.printStackTrace();
            return new Result(false , MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 文章审核不通过
     */
    @RequestMapping("/failPublish.do")
    @ResponseBody
    public Result failPub(@RequestBody TNotice notice){
        try {
            Result result = articleManagerService.failPub(notice);
            return result ;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }


    /**
     * 被举报文章下线
     * @param id
     * @return
     */
    @RequestMapping("/offline.do")
    @ResponseBody
    public Result offLineArticle(Integer id) {
        try {
            Result result = articleManagerService.offLineArticle(id);
            return result ;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 查询一级分类
     */
    @RequestMapping("/findOneCategory.do")
    @ResponseBody
    public Result findOneCategory() {
        try {
            List<TArticleCategory> firstCategory = articleService.findFirstCategory();
            return new Result(true, MessageConstant.OPERATION_SUCCESS,firstCategory);
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

   /*
    * 新增分类
    */
    @RequestMapping("/addCategory.do")
    @ResponseBody
    public Result addCategory(@RequestBody TArticleCategory category){
        try {
            Result result = articleService.addCategory(category);
            return result ;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }


    /*
     * 查询昨天、上周、上月发文总数
     */
    @RequestMapping("/getArticleNumber.do")
    @ResponseBody
    public Result getArticleNumber(){
        try {
            Result result = articleReportService.getArticleNumber();
            return result ;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 获取个一级分类上月发布的文章数量
     */
    @RequestMapping("/getLastMonthCategoryNumber.do")
    @ResponseBody
    public Result getLastMonthCategoryNumber(){
        try {
            Result result = articleReportService.getLastMonthCategoryNumber();
            return result ;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 获取上周发文情况
     */
    @RequestMapping("/getLastWeekArticleNumber.do")
    @ResponseBody
    public Result getLastWeekArticleNumber(){
        try {
            Result result = articleReportService.getLastWeekArticleNumber();
            return result ;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }


    /*
     * 上月最受欢迎技术排名
     */
    @RequestMapping("/getPopularTechnology.do")
    @ResponseBody
    public Result getPopularTechnology(){
        try {
            Result result = articleReportService.getPopularTechnology();
            return result ;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

}
