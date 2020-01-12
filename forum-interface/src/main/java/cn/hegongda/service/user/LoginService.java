package cn.hegongda.service.user;

import cn.hegongda.result.Result;

public interface LoginService {

    // 密码进行登陆
    public Result loginByPass(String username,String password,String token);


    // 短信进行登陆
    public Result loginByMobile(String mobile, String token);

    // 判断手机是否存在，存在则发验证码
    public Result validateMobile(String mobile);

}
