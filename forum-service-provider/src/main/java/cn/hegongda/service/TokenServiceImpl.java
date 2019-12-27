package cn.hegongda.service;

import cn.hegongda.constant.RedisConstant;
import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import cn.hegongda.utils.JsonUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service(interfaceClass = TokenService.class)
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public Result getUserByTokenAndUsernameOrMibile(String token, String usernameOrmobile) {
        if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(usernameOrmobile)){
            // 根据token从redis中获取用户信息
            Jedis jedis = jedisPool.getResource();
            String json = jedis.get(RedisConstant.USER_TOKEN + token);
            //判断用户信息的username是否相同
            if (StringUtils.isNotBlank(json)){
                TUser user = JsonUtils.jsonToPojo(json, TUser.class);
                if(user != null &&( (user.getUsername().equals(usernameOrmobile) || user.getMobile().equals(usernameOrmobile)))){
                    jedis.expire(RedisConstant.USER_TOKEN + token, 60 * 60 * 24);
                    return new Result(true,"登陆成功");
                }
            }

        }
        return new Result(false,"用户第一次登陆");
    }

}
