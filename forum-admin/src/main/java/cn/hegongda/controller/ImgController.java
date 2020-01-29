package cn.hegongda.controller;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.constant.RedisConstant;
import cn.hegongda.pojo.TImg;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.ImgService;
import cn.hegongda.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/img")
public class ImgController {

    @Reference
    private ImgService imgService ;

    @Autowired
    private JedisPool jedisPool;

    /*
     * 上传图片至七牛云
     */
    @RequestMapping("/load.do")
    @ResponseBody
    public Result upload(@RequestBody MultipartFile imgFile){
        try {
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);
        String prefix = UUID.randomUUID().toString().substring(0,28);
        String fileName = prefix + suffix ;
        QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
        // jedis统计上传至云服务器的图片
        Jedis jedis = jedisPool.getResource();
        jedis.sadd(RedisConstant.USER_PIC_YUN_RESORCES , fileName);

            return new Result(true, MessageConstant.OPERATION_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL);
        }
    }

    /*
     * 查询上传图片的所属分类
     */
    @RequestMapping("/findImgCategory.do")
    @ResponseBody
    public Result findImgCategory(){
        try {
            return imgService.findImgCategory();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 将图片保存到本地
     */
    @RequestMapping("/saveImg.do")
    @ResponseBody
    public Result saveImg(@RequestBody List<TImg> list){
        try {
            return imgService.saveImg(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 添加图片分类
     */
    @RequestMapping("/addCategory.do")
    @ResponseBody
    public Result addCategory(String cname){
        try {
            cname = new String(cname.getBytes("ISO8859-1"),"UTF-8");
            return imgService.saveImgCate(cname);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 根据cid查询某一分类下的图片
     */
    @RequestMapping("/findImgByCid.do")
    @ResponseBody
    public PageResult findImgByCid(Integer cid, @RequestBody QueryPageBean queryPageBean){
        try {
            return imgService.findImgByCidAndPage(cid, queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult( MessageConstant.EXCEPTION_MESSAGE,false);
        }
    }

    /*
     * 根据id删除图片
     */

    @RequestMapping("/delImgById.do")
    @ResponseBody
    public Result delImgById(Integer id){
        try {
            return imgService.delImgById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }
}
