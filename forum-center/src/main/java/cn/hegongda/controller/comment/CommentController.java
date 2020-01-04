package cn.hegongda.controller.comment;


import cn.hegongda.constant.MessageConstant;
import cn.hegongda.pojo.CommentExpan;
import cn.hegongda.pojo.TComment;
import cn.hegongda.pojo.TCommentReport;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.CommentService;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Reference
    private CommentService commentService;


    /*
     * 获取谋篇文章的评论
     */
    @RequestMapping("/getComments.do")
    @ResponseBody
    public Result getComments(Integer content_id, Integer type ){
        try {
            Result result = commentService.getComments(content_id, type);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }


    /*
     * 发布评论
     */
    @RequestMapping("/pubComment.do")
    @ResponseBody
    public Result pubComment(@RequestBody CommentExpan commentExpan){
        try {
            Result result = commentService.pubComment(commentExpan);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 根据id进行递归删除评论
     */
    @RequestMapping("/deleteById.do")
    @ResponseBody
    public Result deletById(Long id) {
        try {
            Result result = commentService.deleteById(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }


    /*
     * 投诉评论
     */
    @RequestMapping("/report.do")
    @ResponseBody
    public Result reportComment(@RequestBody TCommentReport commentReport){
        try {
            Result result = commentService.reportComment(commentReport);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 分页根据用户id查询其文章的最新评论
     */
    @RequestMapping( value="getCommentsByPage.do", method = RequestMethod.POST)
    @ResponseBody
    public PageResult getCommentsByPage(Integer customerId, @RequestBody QueryPageBean queryPageBean, Integer type){
        try {
            PageResult result = commentService.getCommentsByPage(customerId, queryPageBean, type);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult( MessageConstant.EXCEPTION_MESSAGE , false);
        }
    }

    /*
     * 为评论点赞
     */
    @RequestMapping("/addSupportNum.do")
    @ResponseBody
    public Result addSuportNum(Integer num, Integer id){
        try {
            Result result = commentService.addSupportNum(num, id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

}
