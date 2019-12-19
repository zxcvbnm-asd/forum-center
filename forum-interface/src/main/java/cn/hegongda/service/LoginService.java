package cn.hegongda.service;

import cn.hegongda.result.Result;

public interface LoginService {

    // 密码进行登陆
    public Result loginByPass(String username,String password);

}
