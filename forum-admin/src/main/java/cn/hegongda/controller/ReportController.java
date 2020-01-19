package cn.hegongda.controller;


import cn.hegongda.constant.MessageConstant;
import cn.hegongda.pojo.TAnnounce;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.AnnounceService;
import cn.hegongda.service.CommentService;
import cn.hegongda.service.report.ReportService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  用于处理举报相关的请求
 */

@Controller
@RequestMapping("/report")
public class ReportController {

    @Reference
    private ReportService reportService ;


    @Reference
    private CommentService commentService;

    @Reference
    private AnnounceService announceService;

    /*
     *  查询举报信息
     */
    @RequestMapping("/getReport.do")
    @ResponseBody
    public PageResult getReport(Integer status, Integer cate, @RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = reportService.getReport(status, cate, queryPageBean);
            return pageResult;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new PageResult(MessageConstant.EXCEPTION_MESSAGE, false);
        }
    }

    /*
     * 举报信息已经处理， 修改其状态
     */
    @RequestMapping("/changeStats.do")
    @ResponseBody
    public Result changeStatus(Integer id){
        try {
            Result result = reportService.changeStatus(id);
            return result ;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new Result(false , MessageConstant.OPERATION_FAIL) ;
        }
    }


    /*
    *  查询评论被举报的信息
    */
    @RequestMapping("/getCommentReport.do")
    @ResponseBody
    public PageResult getCommentReport(Integer status, Integer cate, @RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = reportService.getCommentReport(status, cate, queryPageBean);
            return pageResult;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new PageResult(MessageConstant.EXCEPTION_MESSAGE, false);
        }
    }

    /*
     * 删除被举报的评论
     */
    @RequestMapping("/deleteCommentById.do")
    @ResponseBody
    public Result deleteCommentById(Long id){
        try {
            Result result = commentService.deleteById(id);
            return result;
        }  catch ( Exception e ) {
            e.printStackTrace();
            return new Result(true, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 发布公告
     */
    @RequestMapping("/publishAnnounce.do")
    @ResponseBody
    public Result publishAnnounce(@RequestBody TAnnounce announce){
        try {
            Result result = announceService.publish(announce);
            return result;
        }  catch ( Exception e ) {
            e.printStackTrace();
            return new Result(true, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

}
