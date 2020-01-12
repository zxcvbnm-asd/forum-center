package cn.hegongda.service.user;

import cn.hegongda.result.Result;

public interface TokenService {
    // 登陆时根据token和username进行对比
    public Result getUserByTokenAndUsernameOrMibile(String token, String username);
}
