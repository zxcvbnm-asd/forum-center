package cn.hegongda.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.TUser;
import cn.hegongda.pojo.TUserExample;
import cn.hegongda.result.Result;
import cn.hegongda.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service(interfaceClass = RegisterService.class)
public class RegisterServiceImpl implements RegisterService {


    @Autowired
    private TUserMapper userMapper;

    /*
     * 用户进行注册
     */
    @Override
    @Transactional
    public Result register(TUser user) {
        if(user == null){
            return new Result(false,"非法参数，请按照正常的渠道进行注册");
        }
        // 根据用户名查询用户是否存在，存在则报错
        if(StringUtils.isNotBlank(user.getUsername())){
            int count = userMapper.findCountUserByUserName(user.getUsername());
            if(count > 0){
                return new Result(false,"注册失败，用户名重复");
            }
        }
        // 根据电话查询用户是否存在，存在，则报错
        if(StringUtils.isNotBlank(user.getMobile())){
            int count = userMapper.findCountUserByUserMobile(user.getMobile());
            if(count > 0){
                return new Result(false,"注册失败，一个电话话只能注册一个用户");
            }
        }

        if(StringUtils.isNotBlank(user.getPassword())){
            // 将密码进行MD5加密
            String newPass = MD5Utils.md5(user.getPassword());
            user.setPassword(newPass);

            // 注册时为用户设置默认头像， 设置默认头像
            user.setAvatarName(MessageConstant.USER_AVATARNAME);

            // 保存到数据库
            int number = userMapper.insert(user);
            if(number > 0){
               return new Result(true,"注册成功");
            }
        }
        return new Result(false,"注册失败,可能传入了非法参数");
    }
}
