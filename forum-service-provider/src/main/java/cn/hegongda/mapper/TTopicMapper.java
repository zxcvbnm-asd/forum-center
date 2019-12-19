package cn.hegongda.mapper;

import cn.hegongda.pojo.TTopic;
import cn.hegongda.pojo.TTopicExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TTopicMapper {
    int countByExample(TTopicExample example);

    int deleteByExample(TTopicExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TTopic record);

    int insertSelective(TTopic record);

    List<TTopic> selectByExample(TTopicExample example);

    TTopic selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TTopic record, @Param("example") TTopicExample example);

    int updateByExample(@Param("record") TTopic record, @Param("example") TTopicExample example);

    int updateByPrimaryKeySelective(TTopic record);

    int updateByPrimaryKey(TTopic record);
}