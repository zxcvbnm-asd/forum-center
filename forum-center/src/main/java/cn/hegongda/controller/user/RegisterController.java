package cn.hegongda.controller.user;

import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import cn.hegongda.service.RegisterService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户注册controller
 */
@Controller
@RequestMapping("/user")
public class RegisterController {

    @Reference
    private RegisterService registerService;

    /*
     * 用户注册
     */
    @ResponseBody
    @RequestMapping("/register.do")
    public Result register(@RequestBody  TUser user){
        try{
            Result result = registerService.register(user);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,"系统出现异常请稍后进行注册");
        }
    }
}
