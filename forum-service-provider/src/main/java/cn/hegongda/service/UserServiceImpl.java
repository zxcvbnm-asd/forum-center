package cn.hegongda.service;

import cn.hegongda.constant.RedisConstant;
import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import cn.hegongda.utils.JsonUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl  implements UserService {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private JedisPool jedisPool;

    // 编辑用户信息
    @Override
    public Result editUser(TUser user, String token) {
        int number = userMapper.updateByPrimaryKey(user);
        if(number > 0){
            // 通过jedis的set结构将，将传至服务器的图片记录
            Jedis jedis = jedisPool.getResource();
            jedis.sadd(RedisConstant.USER_PIC_DB_RESORCES,user.getAvatarName());

            // 当用户编辑信息后，用户数据发生变化，将redis中缓存数据清除
            jedis.del(RedisConstant.USER_INFORMATION + user.getId());
            // 将用户登陆的redis中保存信息进行更改
            jedis.set(RedisConstant.USER_TOKEN + token, JsonUtils.objectToJson(user));

            return new Result(true,"信息编辑成功");
        }
        return new Result(false, "信息编辑失败");
    }

    @Override
    public TUser findByMobile(String mobile) {
        return null;
    }


    // 根据id查询用户信息
    @Override
    public TUser getUserById(Integer id) {
        // 先从redis中查询user，查询不到，则到数据
        Jedis jedis = jedisPool.getResource();
        String json = jedis.get(RedisConstant.USER_INFORMATION + id);
        if(StringUtils.isNotBlank(json)){
            TUser user = JsonUtils.jsonToPojo(json, TUser.class);
            return user;
        }

        TUser user = userMapper.selectByPrimaryKey(id);
        // 将第一次查询出来的结果放到redis中
        jedis.set(RedisConstant.USER_INFORMATION + id, JsonUtils.objectToJson(user));
        return user;
    }


}
