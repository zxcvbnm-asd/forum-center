package cn.hegongda.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.constant.RedisConstant;
import cn.hegongda.mapper.TArticleMapper;
import cn.hegongda.mapper.TCommentMapper;
import cn.hegongda.mapper.TCommentReportMapper;
import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.*;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.AbstractTransactionManagementConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private TCommentReportMapper commentReportMapper;

    @Autowired
    private TArticleMapper articleMapper;

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
            parentComment.setAvatarName(user.getAvatarName());
            // 封装其回复信息
            getChildComments(replyComments, content_id, type, parentComment.getId(),parentComment.getCustomerName());

            comments.add(parentComment);
        }
        return new Result(true, MessageConstant.OPERATION_SUCCESS, comments);
    }

    /*
     * 通过分页的方式获取文章的评论信息
     */
    @Override
    public PageResult getCommentsByPage(Integer customerId, QueryPageBean queryPageBean, Integer type){
        if (customerId == null || queryPageBean == null ){
            return new PageResult(MessageConstant.PARAM_NULL_MESSAGE , false);
        }

        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize()) ;
        // 设置list集合，封装一级评论
        List<CommentExpan> comments = new ArrayList<>();
        List<CommentExpan> parentComments = commentMapper.getParentCommentByCustomerId(customerId, type);
        for (CommentExpan parentComment : parentComments) {
            // 封装回复信息
            List<CommentExpan> replyComments  = new ArrayList<>();
            parentComment.setReplyComments(replyComments);
            // 获取评论人信息
            TUser user = userMapper.selectByPrimaryKey(parentComment.getCustomerId());
            parentComment.setCustomerName(user.getUsername());
            parentComment.setAvatarName(user.getAvatarName());

            // 获取其评论的文章的标题
            TArticle article = articleMapper.selectByPrimaryKey(parentComment.getContentId());
            parentComment.setTitle(article.getTitle());

            // 封装其回复信息
            getChildComments(replyComments, parentComment.getContentId(), type, parentComment.getId(),parentComment.getCustomerName());

            comments.add(parentComment);
        }

        PageInfo info = new PageInfo(comments) ;

        return new PageResult(info.getTotal() ,comments, MessageConstant.OPERATION_SUCCESS, true) ;
    }

    // 获取回复信息
    private void getChildComments(List<CommentExpan> replyComments, Integer content_id, Integer type, Long parentId, String customerName) {
        List<CommentExpan> childComments = commentMapper.getChildComments(content_id,type,parentId);
        replyComments.addAll(childComments);
        for (CommentExpan childComment : childComments) {
            // 设置回复人信息
            TUser user = userMapper.selectByPrimaryKey(childComment.getCustomerId());
            childComment.setReplyCustomerName(user.getUsername());
            childComment.setAvatarName(user.getAvatarName());
            // 设置评论人信息
            childComment.setCustomerName(customerName);
            getChildComments(replyComments,content_id, type, childComment.getId(), childComment.getReplyCustomerName());
        }
    }


    /*
     *  发表评论
     */
    @Override
    @Transactional
    public Result pubComment(CommentExpan commentExpan) {
        if (commentExpan == null) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        // 补全参数
        commentExpan.setStatus(1);
        commentExpan.setSupportNum(0);
        commentExpan.setCommentDate(new Date());
        String replyCustomerName = commentExpan.getCustomerName();
        commentMapper.insert(commentExpan);
        if (commentExpan.getParentId() != 0){
            // 查询出评论人
            String customerName = commentMapper.getCustomerName(commentExpan.getParentId());
            commentExpan.setCustomerName(customerName);
            // 设置回复人， 当前评论人
            commentExpan.setReplyCustomerName(replyCustomerName);

        }else {
            List<CommentExpan> replyCommentCustomers = new ArrayList<>();
            commentExpan.setReplyComments(replyCommentCustomers);
        }
        return new Result(true,MessageConstant.OPERATION_SUCCESS,commentExpan);
    }


    /*
     * 根据id删除评论进行递归删除
     */
    @Override
    @Transactional
    public Result deleteById(Long id) {
        if (id == null ){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        commentMapper.deleteByPrimaryKey(id);
        // 先根据id查询出来其子评论
        List<TComment> list = commentMapper.getComments(id);
        for (TComment comment : list) {
            deleteHelpComment(comment.getId());
        }
        return new Result(true,"删除成功");
    }

    // 递归删除评论
    private void deleteHelpComment(Long id) {
        commentMapper.deleteByPrimaryKey(id);
        List<TComment> list = commentMapper.getComments(id);
        for (TComment comment : list) {
            deleteHelpComment(comment.getId());
        }
    }

    /*
     * 投诉评论
     */

    @Override
    @Transactional
    public Result reportComment(TCommentReport commentReport) {
        if (commentReport == null ){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        commentReport.setReportTime(new Date());
        commentReport.setStatus(0);   // 代表没有读取
        Integer number = commentReportMapper.save(commentReport);
        if (number < 1){
            return new Result(false, "投诉失败");
        }
        return new Result(true,MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 为评论进行点赞
     */
    @Override
    @Transactional
    public Result addSupportNum(Integer num, Integer id) {
        if (num == null || id ==null ){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }

        // 先查询出来,在进行更新
        TComment comment = commentMapper.selectByPrimaryKey(Long.valueOf(id + ""));
        if (comment != null ){
            comment.setSupportNum(comment.getSupportNum() + num);
            commentMapper.updateByPrimaryKey(comment);
            return new Result(true, MessageConstant.OPERATION_SUCCESS);
        }
        return new Result(false, MessageConstant.OPERATION_FAIL);
    }
}
