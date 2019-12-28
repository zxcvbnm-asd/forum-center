package cn.hegongda.controller.comment;


import cn.hegongda.constant.MessageConstant;
import cn.hegongda.result.Result;
import cn.hegongda.service.CommentService;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Reference
    private CommentService commentService;

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
}
