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

import javax.jws.WebResult;
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
            // 对时间段的条件进行判断，若存在条件，说明是按照时间段进行查询
            if(queryPageBean.getTimeArray() != null && queryPageBean.getTimeArray().length > 0){
                return articleReportService.searchByTime(id,queryPageBean);
            }
            PageResult pageResult = articleReportService.getDayTotal(id, queryPageBean);
            return pageResult;
        } catch (Exception e){
            e.printStackTrace();
            return new PageResult("系统繁忙",false);
        }
    }


    /*
     *  按照时间段进行查询
     */
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

    /*
     * 查询昨天各类文章的阅读量情况
     */
    @ResponseBody
    @RequestMapping("/showYearsDay.do")
    public Result showYearsDay(Integer id){
        try {
            Result result = articleReportService.showYearsDay(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,"系统出现异常");
        }
    }

    /*
     * 查询上周文章阅读量
     */
    @RequestMapping("/showLastWeek.do")
    @ResponseBody
    public Result getLastWeek(Integer id){
        try {
            Result result = articleReportService.getLastWeek(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "系统出现异常");
        }
    }

    /*
     * 获取前十二个月的阅读量统计
     */
    @RequestMapping("/showPreMonths.do")
    @ResponseBody
    public Result getPreMonthsTotal(Integer id){
        try {
            Result result = articleReportService.getPreMonthsTotal(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "系统出现异常");
        }
    }
}
