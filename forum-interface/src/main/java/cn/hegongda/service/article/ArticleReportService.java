package cn.hegongda.service.article;

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

    // 查询昨天、上周、上月发文总数
    Result getArticleNumber();

   // 获取上月个一级分类发文数量
    Result getLastMonthCategoryNumber();

    // 获取上周发文情况
    Result getLastWeekArticleNumber();

    // 技术受欢迎程度排名
    Result getPopularTechnology();
}
