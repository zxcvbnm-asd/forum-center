package cn.hegongda.mapper;

import cn.hegongda.pojo.TImg;
import cn.hegongda.pojo.TImgExample;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TImgMapper {

    @Select("select id as id,img_url as imgUrl, cid as cid from t_img where cid=#{cid} order by create_tiime desc limit 0,#{num}")
    List<TImg> findImgByCid(@Param("cid") Integer cid, @Param("num") Integer num);

    @Insert("insert into t_img (img_url,create_tiime, cid) values (#{imgUrl},#{createTiime},#{cid})")
    int insert(TImg img);

    @Select("select id as id,img_url as imgUrl, create_tiime as createTiime  from t_img where cid=#{cid} order by create_tiime desc")
    List<TImg> findImgByCidAndPage(Integer cid);

    @Delete("delete from t_img where id=#{id}")
    void delImgById(Integer id);
}