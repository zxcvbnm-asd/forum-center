package cn.hegongda.service;

import cn.hegongda.pojo.TUser;


public interface UserService {

    // 根据id查找用户
    public TUser getUserById(Integer id);

    // 编辑用户
    public int editUser(TUser user);

    // 根据电话号码查找用户
    public TUser findByMobile(String mobile);
}
