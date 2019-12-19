package cn.hegongda.controller;


import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import cn.hegongda.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户相关数据的controller
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;


    /*
     *  登陆main.html页面时，查询用户信息，显示用户头像等信息
     */
    @RequestMapping("/getUserById.do")
    @ResponseBody
    public Result findUserById(Integer id){
        try{
            TUser user = userService.getUserById(id);
            return new Result(true,"用户查询成功",user);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"系统出现错误，请稍后进行查询");
        }
    }
}
