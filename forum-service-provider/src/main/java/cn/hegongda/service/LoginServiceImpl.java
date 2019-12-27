package cn.hegongda.service;

import cn.hegongda.constant.RedisConstant;
import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import cn.hegongda.utils.JsonUtils;
import cn.hegongda.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@Service(interfaceClass = LoginService.class)
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TUserMapper userMapper;


    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private TokenService tokenService;

    /*
     *  用户进行密码登录
     */
    @Override
    public Result loginByPass(String username, String password,String oldToken) {
        // 根据用户名查询该用户是否存在
        TUser user = userMapper.findUserByUsername(username);
        if(user != null){
            // 存在则进行密码校验
            if(!user.getPassword().equals(MD5Utils.md5(password))){
                return new Result(false,"密码不正确");
            }
            // 根据oldToken进行判断
            Result result = tokenService.getUserByTokenAndUsernameOrMibile(oldToken, username);
            if (result.isFlag()){
                return result;
            }
            String token = this.saveUserToRedis(user);
            return new Result(true,"登陆成功",token);
        }
        return new Result(false,"用户不存在");
    }

    @Override
    public Result validateMobile(String mobile) {
        if(StringUtils.isNotBlank(mobile)){
            // 判断是否有该电话的用户存在
            int count = userMapper.findCountUserByUserMobile(mobile);
            if(count > 0){
                return new Result(true,"短信发送成功");
            }
        }
        return new Result(false,"电话号码输入有误，请检查后进行登陆");
    }


    @Override
    public Result loginByMobile(String mobile, String oldToken) {
        // 根据oldToken进行判断
        Result result = tokenService.getUserByTokenAndUsernameOrMibile(oldToken, mobile);
        if (result.isFlag()){
            return result;
        }
        // 以前未登录过重新登陆
        TUser user = userMapper.findUserByMobile(mobile);
        String token = this.saveUserToRedis(user);
        return new Result(true,"短信发送成功",token);
    }

    // 将user信息保存到redis中
    private String saveUserToRedis(TUser user){
        // 校验成功后将,生成token保存到redis端
        Jedis jedis = jedisPool.getResource();
        String token = UUID.randomUUID().toString();
        String json = JsonUtils.objectToJson(user);
        jedis.set(RedisConstant.USER_TOKEN+token,json);
        // 设置过期时间为一天
        jedis.expire(RedisConstant.USER_TOKEN+token, 60 * 60 * 24);
        return token;
    }
}
