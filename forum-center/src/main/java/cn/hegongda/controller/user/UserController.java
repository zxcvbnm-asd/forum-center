package cn.hegongda.controller.user;


import cn.hegongda.constant.MessageConstant;
import cn.hegongda.constant.RedisConstant;
import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import cn.hegongda.service.UserService;
import cn.hegongda.utils.CookieUtils;
import cn.hegongda.utils.JsonUtils;
import cn.hegongda.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.annotations.Instantiator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 用户相关数据的controller
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /*
     *  登陆main.html页面时，查询用户信息，显示用户头像等信息
     */
    @RequestMapping("/getUserById.do")
    @ResponseBody
    public Result findUserById(Integer id){
        try{
            // 根据cookie的token和jedis判断用户先判断用户是否进行登陆
            String token = CookieUtils.getCookieValue(request, RedisConstant.USER_TOKEN);
            Result result = userService.getUserById(id,token);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"系统出现错误，请稍后进行查询");
        }
    }

    /*
     * 通过token获取用户信息
     */
    @RequestMapping(value = "/getUserByToken.do")
    @ResponseBody
    public Result getUserByToken(){
        Jedis jedis = jedisPool.getResource();
        // 从cookie中获取token
        String token = CookieUtils.getCookieValue(request, RedisConstant.USER_TOKEN);
        // 判断token是否存在存在则从jedis中获取用户信息并返回
        if (StringUtils.isNotBlank(token)){
            //将token保存到客户端从新设置其过期时间
            CookieUtils.setCookie(request,response, RedisConstant.USER_TOKEN, token,60 * 60 * 12);
            String json = jedis.get(RedisConstant.USER_TOKEN + token);
            if (StringUtils.isNotBlank(json)){
                TUser user = JsonUtils.jsonToPojo(json, TUser.class);
                jedis.expire(RedisConstant.USER_TOKEN + token, 60*60*24);
                return new Result(true, "用户已经登陆", user);
            }
        }
        return new Result(false, "用户没有登陆");
    }

    /*
     *  上传用户图像
     */
    @RequestMapping("/upload.do")
    @ResponseBody
    public Result uploadImg(@RequestBody MultipartFile imgFile){
        try {
            // 获取文件后缀名
            String filename = imgFile.getOriginalFilename();
            int index = filename.lastIndexOf(".");
            String suffix = filename.substring(index - 1);
            // 随机生成图片名，防止图片上传造成重名
            String prefix = UUID.randomUUID().toString();
            filename = prefix + suffix;
            // 保存图片至七牛云服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);

            // 通过redis的zset数据结构定时删除多余的上传图片
            Jedis jedis = jedisPool.getResource();
            jedis.sadd(RedisConstant.USER_PIC_YUN_RESORCES,filename);
            return new Result(true,"图片上传成功",filename);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }

    /*
     * 用户编辑信息
     */
    @RequestMapping("/edit.do")
    @ResponseBody
    public Result eidtUser(@RequestBody TUser user){
        System.out.println(userService);
        String token = CookieUtils.getCookieValue(request, RedisConstant.USER_TOKEN);
        Result result = userService.editUser(user, token);
        return result;
    }


    /*
     * 修改密码
     */
    @RequestMapping("/modifyPassword.do")
    @ResponseBody
    public Result modifyPassword(@RequestBody TUser user){
        try {
            String token = CookieUtils.getCookieValue(request, RedisConstant.USER_TOKEN);
            Result result = userService.changePassword(user,token);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL);
        }
    }

    /*
     * 用户退出登录
     */
    @RequestMapping("/layout.do")
    @ResponseBody
    public Result layout(){
        try {
            String token = CookieUtils.getCookieValue(request, RedisConstant.USER_TOKEN);
            // 删除jedis缓存
            Jedis jedis = jedisPool.getResource();
            jedis.del(RedisConstant.USER_TOKEN + token);
            // 删除cookie
            CookieUtils.deleteCookie(request, response, RedisConstant.USER_TOKEN);

            return new Result(true, MessageConstant.OPERATION_SUCCESS);
        } catch (Exception e ){
            e.printStackTrace();
            return new Result(false , MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 当点击文章具体详情时，根据id查询作者
     */
    @RequestMapping("/getWriterById.do")
    @ResponseBody
    public Result getWriterById(Integer id) {
        try {
            Result result = userService.getWriterById(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL);
        }
    }
}
