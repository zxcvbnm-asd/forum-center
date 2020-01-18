package cn.hegongda.mapper;

import cn.hegongda.pojo.TArticleCategory;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TArticleCategoryMapper {

    @Select("select * from t_article_category where parent_id=0")
    List<TArticleCategory> findAll();

    @Select("select * from t_article_category where parent_id=#{parentId}")
    List<TArticleCategory> findChildByParentId(Integer parentId);

    TArticleCategory getCategoryNameById(Integer cid);

    @Select("select * from t_article_category where cname=#{cname}")
    TArticleCategory findByCname(String cname);

    @Insert("insert into t_article_category (cname,parent_id,create_time,rmark) values (#{cname},#{parentId},#{createTime},#{rmark})")
    void save(TArticleCategory category);
}