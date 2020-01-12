package cn.hegongda.controller.fan_atten;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.fan_atten.FanAttenService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理粉丝和关注的的controller
 */

@Controller
@RequestMapping("/fan-atten")
public class FanAttenController {

    @Reference
    private FanAttenService fanAttenService;

    /*
     *   判断用户跟文章作者是否为粉丝关系
     */
    @RequestMapping("/relation.do")
    @ResponseBody
    public Result relation(Integer fid, Integer tid){
        try {
            Result result = fanAttenService.JudgeRelation(fid,tid);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 用于关注和取消关注
     */
    @RequestMapping("/attenOrNot.do")
    @ResponseBody
    public Result attenOrNot(Integer fid, Integer tid){
        try {
            Result result = fanAttenService.attenOrNot(fid,tid);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 根据id查询作者的详细的复合信息
     */
    @RequestMapping("/writerDetail.do")
    @ResponseBody
    public Result getWriterDetail(Integer id){
        try {
            Result result = fanAttenService.getWriterDetail(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 根据uid查询做作者发布的书籍信息
     */
    @RequestMapping("/getArticleList.do")
    @ResponseBody
    public Result getArticleList(Integer id){
        try {
            Result result = fanAttenService.getArticleList(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 查询作者的关注
     */
    @RequestMapping(value="/writerOfAttens.do")
    @ResponseBody
    public Result getWriterAtten(Integer id){
        try {
            Result result = fanAttenService.getUserAtten(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 查询作者的粉丝
     */
    @RequestMapping("/writerOfFans.do")
    @ResponseBody
    public Result getWriterFan(Integer id){
        try {
            Result result = fanAttenService.getUserFan(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 分页查询哟用户的关注
     */
    @RequestMapping( value = "/getUserAttens.do",method = RequestMethod.POST)
    @ResponseBody
    public PageResult getUserAttens(Integer id, @RequestBody  QueryPageBean queryPageBean){
        try {
            PageResult pageResult = fanAttenService.getUserAttens(id, queryPageBean);
            return pageResult ;
        } catch (Exception e){
            e.printStackTrace();
            return new PageResult(MessageConstant.EXCEPTION_MESSAGE,false);
        }
    }

    /*
     * 查询粉丝总数
     */
    @RequestMapping("/getSumFans.do")
    @ResponseBody
    public Result getSumFans(Integer id) {
        try {
            Result result = fanAttenService.getSumFans(id);
            return result ;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EXCEPTION_MESSAGE);
        }
    }


    @RequestMapping("/getFansByDays.do")
    @ResponseBody
    public Result getFansByDays(Integer id, Integer days){
        try {
            Result result = fanAttenService.getFansByDays(id, days);
            return result ;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    @RequestMapping("/getFansByTime.do")
    @ResponseBody
    public Result getFansByDays(Integer id, @RequestBody QueryPageBean queryPageBean){
        try {
            Result result = fanAttenService.getFansByTime(id, queryPageBean);
            return result ;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EXCEPTION_MESSAGE);
        }
    }
}
