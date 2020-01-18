package cn.hegongda.service.report;

import cn.hegongda.pojo.Report;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

public interface ReportService {
    // 添加举报信息
    Result addReport(Report report);

    // 查询举报信息
    PageResult getReport(Integer status, Integer cate, QueryPageBean queryPageBean);

    // 修改状态
    Result changeStatus(Integer id);

    // 查看被举报的评论
    PageResult getCommentReport(Integer status, Integer cate, QueryPageBean queryPageBean);
}
