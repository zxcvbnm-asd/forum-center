package cn.hegongda.controller.article;


import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.ArticleReportService;
import cn.hegongda.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;


/**
 * 文章数据统计controller
 */
@Controller
@RequestMapping("/article-report")
public class ArticleReportController {

    @Reference
    private ArticleReportService articleReportService;

    /*
     * 获取每天的阅读量
     */
    @RequestMapping("/getDayTotal.do")
    @ResponseBody
    public PageResult getDayTotal(Integer id, @RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = articleReportService.getDayTotal(id, queryPageBean);
            return pageResult;
        } catch (Exception e){
            e.printStackTrace();
            return new PageResult("系统繁忙",false);
        }
    }


    // 按照时间段进行查询
    @RequestMapping("/searchByTime.do")
    @ResponseBody
    public PageResult searchByTime(Integer id, @RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = articleReportService.searchByTime(id, queryPageBean);
            return pageResult;
        } catch (Exception e){
            e.printStackTrace();
            return new PageResult("系统繁忙",false);
        }
    }
}
