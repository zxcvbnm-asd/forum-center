package cn.hegongda.mapper;

import cn.hegongda.pojo.Report;
import cn.hegongda.pojo.TCommentReport;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TCommentReportMapper {

    Integer save(Report report);

}