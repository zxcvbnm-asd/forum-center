package cn.hegongda.service.user;

import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;

public interface RegisterService {

    // 用户进行注册
    public Result register(TUser user);

}
