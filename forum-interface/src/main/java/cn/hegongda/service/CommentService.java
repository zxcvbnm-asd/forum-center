package cn.hegongda.service;

import cn.hegongda.pojo.CommentExpan;
import cn.hegongda.pojo.TComment;
import cn.hegongda.result.Result;

public interface CommentService {

    // 获取谋篇文章的评论信息
    Result getComments(Integer content_id, Integer type);
    // 发表评论
    Result pubComment(CommentExpan commentExpan);
    // 根据id删除评论
    Result deleteById(Long id);
}
