package cn.hegongda.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.mapper.RuleMapper;
import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.Rule;
import cn.hegongda.pojo.TUser;
import cn.hegongda.result.Result;
import cn.hegongda.service.user.UserManagerService;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Calendar;
import java.util.Date;

@Service(interfaceClass = UserManagerService.class)
public class UserManagerServiceImpl implements UserManagerService {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private RuleMapper ruleMapper ;

    /*
     * 用于用户违规时，进行禁言或者封号
     */
    @Override
    @Transactional
    public Result addRule(Rule rule) {
        if (rule == null || rule.getUid() == null ) {
            return new Result( false, MessageConstant.PARAM_NULL_MESSAGE);
        }

        // 禁言时间为7天
        Date beginTime = new Date();
        Calendar calendar = Calendar.getInstance() ;
        calendar.setTime(beginTime);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date endTime = calendar.getTime();
        rule.setEndTime(endTime);
        rule.setBeginTime(beginTime);

        // xian先查询该用户是否存，
        Rule rule1 = ruleMapper.getRuleByUid(rule.getUid());
        // 存在进行相关的更新即可
        if (rule1 != null ) {
            // 判断文章的状态如果处于禁言或者封号中，则不做任何处理
            if (rule1.getStatus() != 0 ) {
                return new Result( true, MessageConstant.OPERATION_SUCCESS);
            }
            rule1.setBeginTime(beginTime);
            rule1.setEndTime(endTime);
            rule1.setNum(rule1.getNum() + 1);
            rule1.setStatus(rule.getStatus());
            ruleMapper.updateRule(rule1);
        } else {
            // 不存则直接将记录保存到数据库中即可
            rule.setNum(1);
            ruleMapper.addRule(rule);
        }
         // 将用户表的设置成相关的状态
        TUser user = userMapper.selectByPrimaryKey(rule.getUid());
        user.setStatus(rule.getStatus());
        userMapper.updateByPrimaryKey(user);
        return new Result( true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 进行封号
     */
    @Override
    @Transactional
    public Result closeAccount( Rule rule) {
        if ( rule == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }

        // 封号时间为7天
        Date beginTime = new Date();
        Calendar calendar = Calendar.getInstance() ;
        calendar.setTime(beginTime);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        Date endTime = calendar.getTime();
        rule.setEndTime(endTime);
        rule.setBeginTime(beginTime);

        // 根据uid进行查询(如果状态为2，说明已经被封号不进行处理)
        Rule rule1 = ruleMapper.getRuleByUid(rule.getUid());
        if (rule1 != null) {
            if (rule1.getStatus() == 2) {
               return new Result(true, MessageConstant.OPERATION_SUCCESS);
            }
            rule1.setStatus(2);
            rule1.setBeginTime(beginTime);
            rule1.setEndTime(endTime);
            rule1.setNum(rule1.getNum() + 1);
            ruleMapper.updateRule(rule1);
        } else {
            rule.setNum(1);
            rule.setStatus(2);
            ruleMapper.addRule(rule);
        }

        TUser user = userMapper.selectByPrimaryKey(rule.getUid());
        user.setStatus(2);
        userMapper.updateByPrimaryKey(user);
        // 向用户发送短信

        return  new Result( true, MessageConstant.OPERATION_SUCCESS);
    }
}
