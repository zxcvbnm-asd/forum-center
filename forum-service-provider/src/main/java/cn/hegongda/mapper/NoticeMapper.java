package cn.hegongda.mapper;

import cn.hegongda.pojo.TNotice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticeMapper {

    @Select("select * from t_notice where uid=#{id} order by time")
    List<TNotice> getNotices(Integer id);

    @Select("select * from t_notice where id=#{id}")
    TNotice getNoticeById(Integer id);

    void updateNotice(TNotice notice);

    @Insert("insert into t_notice (title,content,time,status,uid) values (#{title},#{content},#{time},#{status},#{uid})")
    Integer addNotice(TNotice notice);
}
