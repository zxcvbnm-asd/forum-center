package cn.hegongda.service;

import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

/**
 * 文章数据分析服务层
 */
public interface ArticleReportService {
    public PageResult getDayTotal(Integer id, QueryPageBean queryPageBean);

    PageResult searchByTime(Integer id, QueryPageBean queryPageBean);

    Result showYearsDay(Integer id);

    Result getLastWeek(Integer id);

    Result getPreMonthsTotal(Integer id);
}
