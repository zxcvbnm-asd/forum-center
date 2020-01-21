package cn.hegongda.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.constant.RedisConstant;
import cn.hegongda.mapper.TImgCategoryMapper;
import cn.hegongda.mapper.TImgMapper;
import cn.hegongda.pojo.TImg;
import cn.hegongda.pojo.TImgCategory;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.utils.JsonUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = ImgService.class)
public class ImgServiceImpl implements ImgService {

    @Autowired
    private TImgMapper imgMapper;

    @Autowired
    private TImgCategoryMapper imgCategoryMapper;

    @Autowired
    private JedisPool jedisPool;


    /*
     * 查询图片分类
     */
    @Override
    public Result findImgCategory() {
        List<TImgCategory> list = imgCategoryMapper.findAll();
        return new Result(true, MessageConstant.EXCEPTION_MESSAGE, list);
    }

    /*
     * 将图片保存到数据库中
     */
    @Override
    @Transactional
    public Result saveImg(List<TImg> list) {
        Jedis jedis = jedisPool.getResource();
        // 当上传的图片为轮播图时，立刻从redis中的缓存中将其删除
        if (list.get(0).getCid() == 1 ) {
            jedis.del(RedisConstant.PIC_CAROUSEL);
        }
        for (TImg img : list) {
            img.setCreateTiime(new Date());
            jedis.sadd(RedisConstant.USER_PIC_DB_RESORCES, img.getImgUrl());
            imgMapper.insert(img);
        }
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 根据分类id查询图片
     */

    @Override
    public Result findImgByCid(Integer cid, Integer num) {
        // 先从jedis中进行查询
        Jedis jedis = jedisPool.getResource();
        if (cid == 1) {
            String json = jedis.get(RedisConstant.PIC_CAROUSEL);
            if (StringUtils.isNotBlank(json)) {
                List<TImg> list = JsonUtils.jsonToList(json, TImg.class);
                return new Result(true, MessageConstant.OPERATION_SUCCESS,list);
            }
        }
        // 查询不到在从数据库中进行查询
        List<TImg> list = imgMapper.findImgByCid(cid, num);
        // 从数据库中查询后存入jedis中
        if (cid == 1) {
            if (list != null || list.size() > 0) {
                String jsonList = JsonUtils.objectToJson(list);
                jedis.set(RedisConstant.PIC_CAROUSEL, jsonList);

            }
        }
        return new Result(true, MessageConstant.OPERATION_SUCCESS, list);
    }

    /*
     * 添加图片分类
     */
    @Override
    @Transactional
    public Result saveImgCate(String cname) {
        if (StringUtils.isBlank(cname)) {
            return new Result(false, MessageConstant.OPERATION_FAIL);
        }
        TImgCategory category = new TImgCategory();
        category.setCname(cname);
        category.setCreateTime(new Date());
        imgCategoryMapper.insert(category);
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 根据cid和分页进行查询
     */
    @Override
    public PageResult findImgByCidAndPage(Integer cid, QueryPageBean queryPageBean) {
        if (cid == null || queryPageBean == null ) {
            return new PageResult(MessageConstant.OPERATION_FAIL, false);
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<TImg> list = imgMapper.findImgByCidAndPage(cid);
        PageInfo info = new PageInfo(list);
        return new PageResult(info.getTotal(), list, MessageConstant.OPERATION_SUCCESS, true);
    }

    /*
     * 根据id删除图片
     */
    @Override
    @Transactional
    public Result delImgById(Integer id) {
        if (id == null) {
            return new Result(false, MessageConstant.OPERATION_FAIL);
        }
        imgMapper.delImgById(id);
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }
}
