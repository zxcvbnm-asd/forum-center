package cn.hegongda.mapper;

import cn.hegongda.pojo.TCarousel;
import cn.hegongda.pojo.TCarouselExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TCarouselMapper {
    int countByExample(TCarouselExample example);

    int deleteByExample(TCarouselExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TCarousel record);

    int insertSelective(TCarousel record);

    List<TCarousel> selectByExample(TCarouselExample example);

    TCarousel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TCarousel record, @Param("example") TCarouselExample example);

    int updateByExample(@Param("record") TCarousel record, @Param("example") TCarouselExample example);

    int updateByPrimaryKeySelective(TCarousel record);

    int updateByPrimaryKey(TCarousel record);
}