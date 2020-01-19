package cn.hegongda.mapper;

import cn.hegongda.pojo.TAnnounce;
import cn.hegongda.pojo.TAnnounceExample;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TAnnounceMapper {
    int countByExample(TAnnounceExample example);

    int deleteByExample(TAnnounceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TAnnounce record);

    int insertSelective(TAnnounce record);

    List<TAnnounce> selectByExampleWithBLOBs(TAnnounceExample example);

    List<TAnnounce> selectByExample(TAnnounceExample example);

    TAnnounce selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TAnnounce record, @Param("example") TAnnounceExample example);

    int updateByExampleWithBLOBs(@Param("record") TAnnounce record, @Param("example") TAnnounceExample example);

    int updateByExample(@Param("record") TAnnounce record, @Param("example") TAnnounceExample example);

    int updateByPrimaryKeySelective(TAnnounce record);

    int updateByPrimaryKeyWithBLOBs(TAnnounce record);

    int updateByPrimaryKey(TAnnounce record);

    @Select("select * from t_announce order by time desc")
    List<TAnnounce> getAnnouncesByPage();

    @Select("select status from t_user_announce where uid=#{id} and aid=#{aid}")
    Integer getStatus(@Param("id") Integer id,  @Param("aid") Integer aid);

    @Select("select count(*) from t_user_announce where  uid=#{uid} and aid=#{aid}")
    Integer existsUserAnnouce(@Param("uid") Integer uid, @Param("aid") Integer aid);


    @Insert("insert into t_user_announce (uid, aid, status) values (#{uid}, #{aid}, 1)")
    void changeStatus(@Param("uid") Integer uid, @Param("aid") Integer aid);
}