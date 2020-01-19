package cn.hegongda.service;

import cn.hegongda.pojo.TAnnounce;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

public interface AnnounceService {

    // 分页查询公告
    PageResult getByPages(Integer id, QueryPageBean queryPageBean);

    //公告用户已经读了，设置为 status 1
    Result changeStatus(Integer uid, Integer aid);

    // 分页查询用户通知
    PageResult getNotices(Integer id, QueryPageBean queryPageBean);

    // 修改通知状态
    Result changeNoticeStatus(Integer id);

    // 发布公告
    Result publish(TAnnounce announce);
}
