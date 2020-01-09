package cn.hegongda.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.constant.RedisConstant;
import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import cn.hegongda.utils.JsonUtils;
import cn.hegongda.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

            // 将用户登陆的redis中保存信息进行更改
            jedis.set(RedisConstant.USER_TOKEN + token, JsonUtils.objectToJson(user));
            jedis.expire(RedisConstant.USER_TOKEN + token, 60*60*12);
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
    public Result getUserById(Integer id, String token) {
        if ( StringUtils.isBlank(token) || id== null ) {
            return new Result(false, MessageConstant.OPERATION_FAIL);
        }
        // 先从redis中查询user，查询不到，则到数据
        Jedis jedis = jedisPool.getResource();
        String json = jedis.get(RedisConstant.USER_TOKEN + token);
        if(StringUtils.isNotBlank(json)){
            TUser user = JsonUtils.jsonToPojo(json, TUser.class);
            if (user.getId() == id) {
                return new Result( true, MessageConstant.OPERATION_SUCCESS ,user);
            }
        }
        return new Result(false , MessageConstant.OPERATION_FAIL);
    }

    /*
     * 用户修改密码
     */
    @Override
    public Result changePassword(TUser user, String token) {
        if(user == null || StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        String oldPass = user.getPassword().split("-")[0];
        String newPass = user.getPassword().split("-")[1];
        // 先根据用户名查数据库看是否存在该用户
        TUser user1 = userMapper.findUserByUsername(user.getUsername());
        if ( user1 != null ) {
            // 存在该用户判断密码是否相同
            if (user1.getPassword().equals(MD5Utils.md5(oldPass))){
                // 相同更新数据库字段
                user1.setPassword(MD5Utils.md5(newPass));
                userMapper.updateByPrimaryKey(user1);

                // 删除redis中存在的
                Jedis jedis = jedisPool.getResource();
                jedis.del(RedisConstant.USER_TOKEN + token);
                return new Result(true, MessageConstant.OPERATION_SUCCESS);
            }
        }
        return new Result(false, MessageConstant.USER_MODIFY_PASSWORD);
    }


    /*
     * 根据id获取作者信息
     */
    @Override
    public Result getWriterById(Integer id) {
        if ( id == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        TUser writer = userMapper.selectByPrimaryKey(id);
        if ( writer != null ) {
            return new Result(true, MessageConstant.OPERATION_SUCCESS,writer);
        }
        return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
    }
}
