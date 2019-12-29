package cn.hegongda.controller.comment;


import cn.hegongda.constant.MessageConstant;
import cn.hegongda.pojo.CommentExpan;
import cn.hegongda.pojo.TComment;
import cn.hegongda.result.Result;
import cn.hegongda.service.CommentService;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
