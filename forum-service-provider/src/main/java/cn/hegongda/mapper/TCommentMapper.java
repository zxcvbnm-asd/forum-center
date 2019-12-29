package cn.hegongda.mapper;

import cn.hegongda.pojo.CommentExpan;
import cn.hegongda.pojo.TComment;
import cn.hegongda.pojo.TCommentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TCommentMapper {
    int countByExample(TCommentExample example);

    int deleteByExample(TCommentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TComment record);

    int insertSelective(TComment record);

    List<TComment> selectByExample(TCommentExample example);

    TComment selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TComment record, @Param("example") TCommentExample example);

    int updateByExample(@Param("record") TComment record, @Param("example") TCommentExample example);

    int updateByPrimaryKeySelective(TComment record);

    int updateByPrimaryKey(TComment record);

    // 获取评论信息
    List<CommentExpan> getParentComment(@Param("content_id") Integer content_id, @Param("type") Integer type);
    // 获取回复信息
    List<CommentExpan> getChildComments(@Param("content_id") Integer content_id, @Param("type")Integer type,@Param("parentId") Long parentId);

    @Select("SELECT u.username FROM t_comment t INNER JOIN t_user u  ON t.id=#{parentId} AND t.customer_id=u.id")
    String getCustomerName(Long parentId);

    @Select("select * from t_comment where parent_id=#{id}")
    List<TComment> getComments(Long id);
}