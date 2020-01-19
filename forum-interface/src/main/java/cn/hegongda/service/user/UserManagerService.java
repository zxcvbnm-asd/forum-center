package cn.hegongda.service.user;

import cn.hegongda.pojo.Rule;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

public interface UserManagerService {
    // 用于用户违规时，进行禁言或者封号
    Result addRule(Rule rule);

    // 封号
    Result closeAccount( Rule rule);

    // 通过type或者条件进行分页查询
    PageResult findUserByTypr(Integer type, QueryPageBean queryPageBean);

    // 通过status进行查询用户信息
    PageResult findUserByStatus(Integer status, QueryPageBean queryPageBean);

    // 解除禁言
    Result lift(Integer id);
}
