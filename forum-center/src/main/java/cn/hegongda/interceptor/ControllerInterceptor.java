package cn.hegongda.interceptor;

import cn.hegongda.constant.RedisConstant;
import cn.hegongda.pojo.TUser;
import cn.hegongda.utils.CookieUtils;
import cn.hegongda.utils.JsonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerInterceptor implements HandlerInterceptor{

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private HttpServletResponse res;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("请求已经拦截");
        // 从客户端获取token
        String token = CookieUtils.getCookieValue(request, RedisConstant.USER_TOKEN);
        if(StringUtils.isBlank(token)){
            // token不存在则跳转到登录页面
            res.sendRedirect("http://localhost:8082/pages/user/login.html");
            return false;
        }

        // 在redis中取出用户信息
        Jedis jedis = jedisPool.getResource();
        String json = jedis.get(RedisConstant.USER_TOKEN + token);
        if( StringUtils.isBlank(json)){
            request.getRequestDispatcher("pages/login.html").forward(request,response);
            return false;
        }
        TUser user = JsonUtils.jsonToPojo(json, TUser.class);
        jedis.expire(RedisConstant.USER_TOKEN + token, 60 * 60 * 12);
        request.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
