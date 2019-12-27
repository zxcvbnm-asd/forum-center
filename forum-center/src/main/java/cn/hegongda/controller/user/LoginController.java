package cn.hegongda.controller.user;

import cn.hegongda.constant.RedisConstant;
import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import cn.hegongda.service.LoginService;
import cn.hegongda.utils.CookieUtils;
import cn.hegongda.utils.SMSUtils;
import cn.hegongda.utils.ValidateCodeUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录controller
 */
@Controller
@RequestMapping("/user")

public class LoginController {

    @Reference
    private LoginService loginService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private JedisPool jedisPool;

    /*
     * 跳转到登录页面
     */
    @RequestMapping("/login.do")
    public String toLoginPage(){
        return "user/login";
    }

    /*
     * 通过密码的方式进行登陆
     */
    @RequestMapping("/loginByPass.do")
    @ResponseBody
    public Result loginByPass(@RequestBody  TUser user){
        // 判断传入参数是否合法
        if(user == null || StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return new Result(false,"数据发生错误，请检查姓名或者密码是否为空");
        }
        try{
            String token = CookieUtils.getCookieValue(request, RedisConstant.USER_TOKEN);
            Result result = loginService.loginByPass(user.getUsername(),user.getPassword(),token);
            if(result.isFlag() && result.getData() != null){
                // 将token保存到客户端
                CookieUtils.setCookie(request,response, RedisConstant.USER_TOKEN, (String) result.getData(),60 * 60 * 12);
            }
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,"系统出现故障，请稍后登陆");
        }
    }


    /*
     * 通过短信进行登陆
     */
    @RequestMapping("/getValidateCode.do")
    @ResponseBody
    public Result loginByMobile(String mobile){
        try{
            Result result = loginService.validateMobile(mobile);
            if(result.isFlag()){
                // 生成验证码保存到redis中
                String validateCode = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
                Jedis jedis = jedisPool.getResource();
                // 设置过期时间60s
                jedis.setex(RedisConstant.VALIDATE_CODE + mobile,60, validateCode);
                // 发送短信
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, mobile, validateCode);
            }
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,"系统出现故障，请稍后进行登陆");
        }
    }

    /*
     * 短信登陆
     */
    @RequestMapping("/loginByMobile.do")
    @ResponseBody
    public Result loginByMobile(String mobile, String validateCode){
        // 参数校验
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(validateCode)){
            return new Result(false,"请正确填写相关信息");
        }
        try {
            // 判断验证码是否正确
            Jedis jedis = jedisPool.getResource();
            String code = jedis.get(RedisConstant.VALIDATE_CODE + mobile);
            if (validateCode.equals(code)){
                String token = CookieUtils.getCookieValue(request, RedisConstant.USER_TOKEN);
                Result result = loginService.loginByMobile(mobile,token);
                // 说明用户已经提前就登陆过
                if (result.getData() == null){
                    return result;
                }
                // 将token保存到客户端
                CookieUtils.setCookie(request,response, RedisConstant.USER_TOKEN, (String) result.getData(),60 * 60 * 12);
                return result;
            }
            return new Result(false,"验证码不正确");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"系统出现错误，请稍后进行登陆");
        }
    }
}
