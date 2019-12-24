package cn.hegongda.mapper;

import cn.hegongda.pojo.TArticle;
import cn.hegongda.pojo.TArticleExample;
import java.util.List;
import java.util.Map;

import cn.hegongda.result.QueryPageBean;
import org.apache.ibatis.annotations.Param;

public interface TArticleMapper {
    int countByExample(TArticleExample example);

    int deleteByExample(TArticleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TArticle record);

    int insertSelective(TArticle record);

    List<TArticle> selectByExampleWithBLOBs(TArticleExample example);

    List<TArticle> selectByExample(TArticleExample example);

    TArticle selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TArticle record, @Param("example") TArticleExample example);

    int updateByExampleWithBLOBs(@Param("record") TArticle record, @Param("example") TArticleExample example);

    int updateByExample(@Param("record") TArticle record, @Param("example") TArticleExample example);

    int updateByPrimaryKeySelective(TArticle record);

    int updateByPrimaryKeyWithBLOBs(TArticle record);

    int updateByPrimaryKey(TArticle record);

    int add(TArticle article);
    // 查询全部文章
    List<Map> findAllByPage(String queryString);

    // 查询每天阅读量
    List<Map> getDayTotal(Integer id);
    // 按照时间段进行查询
    List<Map> searchByTime(Map<String, String> map);
}