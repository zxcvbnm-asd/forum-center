package cn.hegongda.service;

import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

public interface FanAttenService {

    // 判断用户跟文章作者是否为粉丝关系
    Result JudgeRelation(Integer fid, Integer tid);
    // 用于关注和取消关注
    Result attenOrNot(Integer fid, Integer tid);
    // 查询作者的详细信息
    Result getWriterDetail(Integer id);
    // 查询作者发布的全部文章
    Result getArticleList(Integer id);
    // 作者关注
    Result getUserAtten(Integer id);
    // 作者粉丝
    Result getUserFan(Integer id);
    // 分页加条件查询
    PageResult getUserAttens(Integer id, QueryPageBean queryPageBean);

    // 查询粉丝总数
    Result getSumFans(Integer id);
    // 根据天数查询粉丝
    Result getFansByDays(Integer id, Integer days);
    // 根据时间段查询粉丝增长情况
    Result getFansByTime(Integer id, QueryPageBean queryPageBean);
}
