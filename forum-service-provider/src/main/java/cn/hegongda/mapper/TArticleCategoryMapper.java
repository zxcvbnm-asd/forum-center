package cn.hegongda.mapper;

import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.pojo.TArticleCategoryExample;
import cn.hegongda.pojo.TArticleCategoryWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TArticleCategoryMapper {
    int countByExample(TArticleCategoryExample example);

    int deleteByExample(TArticleCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TArticleCategoryWithBLOBs record);

    int insertSelective(TArticleCategoryWithBLOBs record);

    List<TArticleCategoryWithBLOBs> selectByExampleWithBLOBs(TArticleCategoryExample example);

    List<TArticleCategory> selectByExample(TArticleCategoryExample example);

    TArticleCategoryWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TArticleCategoryWithBLOBs record, @Param("example") TArticleCategoryExample example);

    int updateByExampleWithBLOBs(@Param("record") TArticleCategoryWithBLOBs record, @Param("example") TArticleCategoryExample example);

    int updateByExample(@Param("record") TArticleCategory record, @Param("example") TArticleCategoryExample example);

    int updateByPrimaryKeySelective(TArticleCategoryWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TArticleCategoryWithBLOBs record);

    int updateByPrimaryKey(TArticleCategory record);
}