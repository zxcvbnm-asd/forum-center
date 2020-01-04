package cn.hegongda.service;

import cn.hegongda.pojo.CommentExpan;
import cn.hegongda.pojo.TComment;
import cn.hegongda.pojo.TCommentReport;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

public interface CommentService {

    // 获取谋篇文章的评论信息
    Result getComments(Integer content_id, Integer type);
    // 发表评论
    Result pubComment(CommentExpan commentExpan);
    // 根据id删除评论
    Result deleteById(Long id);
    // 投诉评论
    Result reportComment(TCommentReport commentReport);
    //分页查询评论
    PageResult getCommentsByPage(Integer customerId, QueryPageBean queryPageBean, Integer type);
    // 为评论进行点赞
    Result addSupportNum(Integer num, Integer id);
}
