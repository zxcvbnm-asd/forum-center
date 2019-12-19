package cn.hegongda.service;

import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.TUser;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

@Service
public class UserServiceImpl  implements UserService {

    @Autowired
    private TUserMapper userMapper;


    @Override
    public TUser getUserById(Integer id) {
        TUser user = userMapper.selectByPrimaryKey(1);
        return user;
    }

    @Override
    public int updateUser(TUser user) {
        return 0;
    }
}
