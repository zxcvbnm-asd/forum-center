package cn.hegongda.service;

import cn.hegongda.result.Result;

public interface CommentService {

    // 获取谋篇文章的评论信息
    Result getComments(Integer content_id, Integer type);
}
