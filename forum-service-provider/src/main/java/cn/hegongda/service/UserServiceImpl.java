package cn.hegongda.service;

import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.TUser;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

@Service
public class UserServiceImpl  implements UserService {
    @Override
    public int editUser(TUser user) {
        return 0;
    }

    @Override
    public TUser findByMobile(String mobile) {
        return null;
    }

    @Autowired
    private TUserMapper userMapper;


    @Override
    public TUser getUserById(Integer id) {
        TUser user = userMapper.selectByPrimaryKey(1);
        return user;
    }
}
