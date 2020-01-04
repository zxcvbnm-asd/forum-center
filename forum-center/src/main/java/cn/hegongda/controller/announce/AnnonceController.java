package cn.hegongda.controller.announce;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.AnnounceService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  公告
 */
@Controller
@RequestMapping("/announce")
public class AnnonceController {

    @Reference
    private AnnounceService announceService ;

    /*
    *  分页查询公告
    */
    @RequestMapping(value = "/getByPage.do" , method = RequestMethod.POST)
    @ResponseBody
    public PageResult getAnnconces(Integer id, @RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = announceService.getByPages(id, queryPageBean);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult( MessageConstant.EXCEPTION_MESSAGE, false);
        }
    }


    /*
     * 公告用户已经读了，设置为 status 1
     */
    @RequestMapping("/changStatus.do")
    @ResponseBody
    public Result changeStatus(Integer uid , Integer aid){
        try {
            Result result = announceService.changeStatus( uid , aid);
            return result ;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.EXCEPTION_MESSAGE) ;
        }
    }

    /*
     * 分页查询用户的通知
     */
    @RequestMapping("/getNotices.do")
    @ResponseBody
    public PageResult getNotices(Integer id, @RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = announceService.getNotices(id, queryPageBean);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult( MessageConstant.EXCEPTION_MESSAGE, false);
        }
    }

    /*
     * 用户点击通知时， 设置通知已读
     */
    @RequestMapping("/changeNoticeStatus.do")
    @ResponseBody
    public Result changeNoticeStatus(Integer id){
        try {
            Result result = announceService.changeNoticeStatus( id );
            return result ;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.EXCEPTION_MESSAGE) ;
        }
    }

}
