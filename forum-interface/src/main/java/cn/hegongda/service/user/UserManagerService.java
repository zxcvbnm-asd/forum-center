package cn.hegongda.service.user;

import cn.hegongda.pojo.Rule;
import cn.hegongda.result.Result;

public interface UserManagerService {
    // 用于用户违规时，进行禁言或者封号
    Result addRule(Rule rule);

    // 封号
    Result closeAccount( Rule rule);
}
