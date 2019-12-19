package cn.hegongda.mapper;

import cn.hegongda.pojo.TAnnounce;
import cn.hegongda.pojo.TAnnounceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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
}