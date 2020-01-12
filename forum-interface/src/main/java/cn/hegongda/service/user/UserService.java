package cn.hegongda.service.user;

import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;


public interface UserService {

    // 根据id查找用户
    public Result getUserById(Integer id, String token);

    // 编辑用户
    public Result editUser(TUser user, String token);

    // 根据电话号码查找用户
    public TUser findByMobile(String mobile);

    // 用户修改密码
    public Result changePassword(TUser user, String token);

    // 根据id获取作者信息
    Result getWriterById(Integer id);
}
