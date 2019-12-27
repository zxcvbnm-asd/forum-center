package cn.hegongda.mapper;

import cn.hegongda.pojo.TArticle;
import cn.hegongda.pojo.TArticleExample;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hegongda.result.QueryPageBean;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    // 查询昨日数据
    List<Map> getYesterDay(Map<String, String> map);
    // 获取上周数据
    List<Integer> getLastWeekNumber(Map<String, String> map);
    // 获取前十二个月的数据
    List<Integer> getPreMonths(Map<String, String> conditionMap);
    // 首页查询出分类阅读量最多的文章
    List<TArticle> findMaxNumArticle(Integer id);
    //查询文章点击量
    Integer getSupportNum(Integer aid);
    // 向点赞表中出入数据

    @Insert("INSERT INTO t_article_expan (support_num,aid,attention) VALUES (#{count},#{aid},NULL)")
    void insertArticleExpan(@Param("count") int count,@Param("aid") Integer aid);
}