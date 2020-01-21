package cn.hegongda.controller;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.result.Result;
import cn.hegongda.service.ImgService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProtalController {

    @Reference
    private ImgService imgService;

    @RequestMapping("/protal.do")
    public String toPaotalPage(){
        return "protal";
    }

    /*
     * 查询轮播图
     */
    @RequestMapping("/findCarousel.do")
    @ResponseBody
    public Result findCarousel(Integer cid, Integer num){
        try {
            return imgService.findImgByCid(cid,num);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }
}
