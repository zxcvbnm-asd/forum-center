package cn.hegongda.mapper;

import cn.hegongda.pojo.ArticleExpan;
import cn.hegongda.pojo.ArticlePub;
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
import org.apache.ibatis.annotations.Update;

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

    @Update("UPDATE t_article SET num =num + 1 WHERE id=#{id}")
    void addReadNum(Integer id);

    // 将阅读记录插入到数据中
    void readRecoderToDatabase(@Param("total") Integer total, @Param("uid") String uid, @Param("cid") String cid, @Param("date") String date);

    // 查询状态为二的所有文章信息

    List<TArticle>  findPublished();

    // 向logstash中插入数据
    @Insert("INSERT INTO t_article_pub  (id,title,num,labels,cover_url,pub_time,uid,cid,username,cname,`timestamp`,parent_id)   \n" +
            "VALUES (#{id},#{title},#{num},#{labels},#{coverUrl},#{pubTime},#{uid},#{cid},#{username},#{cname},#{timestamp},#{parentId})\n")
    void addArticlePub(ArticlePub articlePub);

    @Select(" SELECT SUM(total)\n" +
            " FROM t_read_recoder\n" +
            " GROUP BY `date`,uid HAVING uid=#{id} AND `date`=#{datetime}")
    Integer getDayNumbers(@Param("id") Integer id, @Param("datetime") String datetime);

    // 按条件和分页查询出待审核的文章
    List<ArticleExpan> getAritcleByStatus(@Param("status") Integer status,@Param("queryString") String queryString);

    // 查询昨天的发文总数
    Integer getYearsToday(String dateStr);

    // 获取上周发文总数
    Integer getLastWeekArticleNumber(@Param("monday") String monday, @Param("sunday") String sunday);

    // 获取上月发文总数
    @Select("SELECT COUNT(*) FROM t_article WHERE  DATE_FORMAT(pub_time,'%Y-%m')=#{month} AND status=2")
    Integer getLastMonthArticleNumber(String month);

    // 获取上月一级分类发文总数
    Integer getLastMonthCategoryArticleNumber(@Param("month") String month, @Param("id") Integer id);

    @Select("SELECT COUNT(*) AS total,cid FROM t_article  WHERE DATE_FORMAT(pub_time,'%Y-%m')=#{month} AND status=2 GROUP BY cid ORDER BY total DESC LIMIT 0,9")
    List<Map> getPopularTechonolgy(String month);
}