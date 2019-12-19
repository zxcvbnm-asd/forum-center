package cn.hegongda.service;

import cn.hegongda.pojo.TUser;


public interface UserService {

    public TUser getUserById(Integer id);

    public int updateUser(TUser user);
}
