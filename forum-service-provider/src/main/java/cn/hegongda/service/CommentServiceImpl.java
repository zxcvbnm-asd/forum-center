package cn.hegongda.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.mapper.TCommentMapper;
import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.CommentExpan;
import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 *  评论的服务层
 */
@Service(interfaceClass = CommentService.class)
public class CommentServiceImpl implements CommentService {

    @Autowired
    private TCommentMapper commentMapper;

    @Autowired
    private TUserMapper userMapper;

    /*
     * 获取文章的评论信息
     */
    @Override
    public Result getComments(Integer content_id, Integer type) {
        // 设置list集合，封装一级评论
        List<CommentExpan> comments = new ArrayList<>();
        List<CommentExpan> parentComments = commentMapper.getParentComment(content_id, type);
        for (CommentExpan parentComment : parentComments) {
            // 封装回复信息
            List<CommentExpan> replyComments  = new ArrayList<>();
            parentComment.setReplyComments(replyComments);
            // 获取评论人信息
            TUser user = userMapper.selectByPrimaryKey(parentComment.getCustomerId());
            parentComment.setCustomerName(user.getUsername());
            // 封装其回复信息
            getChildComments(replyComments, content_id, type, parentComment.getId(),parentComment.getCustomerName());

            comments.add(parentComment);
        }
        return new Result(true, MessageConstant.OPERATION_SUCCESS, comments);
    }


    // 获取回复信息
    private void getChildComments(List<CommentExpan> replyComments, Integer content_id, Integer type, Long parentId, String customerName) {
        List<CommentExpan> childComments = commentMapper.getChildComments(content_id,type,parentId);
        replyComments.addAll(childComments);
        for (CommentExpan childComment : childComments) {
            // 设置回复人信息
            TUser user = userMapper.selectByPrimaryKey(childComment.getCustomerId());
            childComment.setReplyCustomerName(user.getUsername());
            // 设置评论人信息
            childComment.setCustomerName(customerName);
            getChildComments(replyComments,content_id, type, parentId, childComment.getReplyCustomerName());
        }
    }
}
