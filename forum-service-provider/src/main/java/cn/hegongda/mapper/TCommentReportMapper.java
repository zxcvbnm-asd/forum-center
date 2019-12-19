package cn.hegongda.mapper;

import cn.hegongda.pojo.TCommentReport;
import cn.hegongda.pojo.TCommentReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TCommentReportMapper {
    int countByExample(TCommentReportExample example);

    int deleteByExample(TCommentReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TCommentReport record);

    int insertSelective(TCommentReport record);

    List<TCommentReport> selectByExampleWithBLOBs(TCommentReportExample example);

    List<TCommentReport> selectByExample(TCommentReportExample example);

    TCommentReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TCommentReport record, @Param("example") TCommentReportExample example);

    int updateByExampleWithBLOBs(@Param("record") TCommentReport record, @Param("example") TCommentReportExample example);

    int updateByExample(@Param("record") TCommentReport record, @Param("example") TCommentReportExample example);

    int updateByPrimaryKeySelective(TCommentReport record);

    int updateByPrimaryKeyWithBLOBs(TCommentReport record);

    int updateByPrimaryKey(TCommentReport record);
}