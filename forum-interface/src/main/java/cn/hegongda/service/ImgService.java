package cn.hegongda.service;

import cn.hegongda.pojo.TImg;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

import java.util.List;

public interface ImgService {
    // 查询分类
    Result findImgCategory();

    // 将图片保存到数据库中
    Result saveImg(List<TImg> list);

     // 根据分类id查询图片
    Result findImgByCid(Integer cid, Integer num);

    // 添加图片分类
    Result saveImgCate(String cname);

    // 根据cid和分页进行查询
    PageResult findImgByCidAndPage(Integer cid, QueryPageBean queryPageBean);

    // 根据id删除图片
    Result delImgById(Integer id);
}
