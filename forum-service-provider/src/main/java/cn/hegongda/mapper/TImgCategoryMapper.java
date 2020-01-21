package cn.hegongda.mapper;

import cn.hegongda.pojo.TImgCategory;
import cn.hegongda.pojo.TImgCategoryExample;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TImgCategoryMapper {
    int countByExample(TImgCategoryExample example);

    int deleteByExample(TImgCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TImgCategory record);

    int insertSelective(TImgCategory record);

    List<TImgCategory> selectByExample(TImgCategoryExample example);

    TImgCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TImgCategory record, @Param("example") TImgCategoryExample example);

    int updateByExample(@Param("record") TImgCategory record, @Param("example") TImgCategoryExample example);

    int updateByPrimaryKeySelective(TImgCategory record);

    int updateByPrimaryKey(TImgCategory record);

    @Select("select * from t_img_category")
    List<TImgCategory> findAll();

}