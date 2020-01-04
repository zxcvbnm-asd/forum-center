package cn.hegongda.mapper;

import com.alibaba.dubbo.config.annotation.Service;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FanAttenMapper {


    @Select("SELECT COUNT(*) FROM t_atten_fan WHERE fid=#{fid} AND tid=#{tid}")
    Integer isExists(@Param("fid") Integer fid, @Param("tid") Integer tid);

    @Delete("DELETE FROM t_atten_fan WHERE fid=#{fid} AND tid=#{tid}")
    void unsubscribe(@Param("fid") Integer fid, @Param("tid") Integer tid);

    @Insert("INSERT INTO t_atten_fan (fid, tid,atten_time) VALUES (#{fid},#{tid},#{date})")
    void subscribe(@Param("fid") Integer fid, @Param("tid") Integer tid, @Param("date") Date date);

    // 根据id查询详细信息
    Map getWriterDetail(Integer id);
    // 根据id查询详作者发布的信息
    List<Map> getArticleList(Integer id);
    // 获取作者关注的人数
    List<Map> getUserAtten(Integer id);
    // 获取作者的粉丝
    List<Map> getUserFan(Integer id);
    // 分页获取关注数
    List<Map> getUserAttens(@Param("id") Integer id, @Param("queryString") String queryString);

    @Select("SELECT COUNT(*) FROM t_atten_fan WHERE tid=#{id}")
    Integer getSumFans(Integer id);

    Integer getFansCountByDays(@Param("date") String date, @Param("id") Integer id);
}
