package cn.hegongda.controller.fan_atten;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.result.Result;
import cn.hegongda.service.FanAttenService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
