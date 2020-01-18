package cn.hegongda.mapper;

import cn.hegongda.pojo.Report;
import cn.hegongda.pojo.TNotice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ReportMapper {


    @Insert("INSERT INTO t_report (content,`type`,report_time,`status`,cate,cid,uid) VALUES (#{content},#{type},#{reportTime},#{status},#{cate},#{cid},#{uid})")
    Integer addReport(Report report);


    // 获取举报信息
    List<Map> getReport(@Param("status") Integer status, @Param("cate") Integer cate , @Param("queryString") String queryString);

    @Select("select * from t_report where id=#{id}")
    Report getReportById(Integer id);

    void updateRepoert(Report report);

    List<Map> getCommentReport(@Param("status") Integer status, @Param("cate") Integer cate);
}
