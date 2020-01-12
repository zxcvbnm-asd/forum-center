package cn.hegongda.controller;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.pojo.TNotice;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.article.ArticleManagerService;

import cn.hegongda.service.article.ArticleService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/article-manager")
public class ArticleManagerController {

    @Reference
    private ArticleManagerService articleManagerService;


    @Reference
    private ArticleService articleService;


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

}
