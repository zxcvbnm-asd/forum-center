package cn.hegongda.controller;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.pojo.Rule;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.user.UserManagerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  后台管理用户controller
 */
@Controller
@RequestMapping("/user-manager")
public class UserManagerController {

    @Reference
    private UserManagerService userManagerService;


    /*
     * 用于用户违规时，进行禁言
     */
    @RequestMapping("/againstRule.do")
    @ResponseBody
    public Result addRule(@RequestBody  Rule rule){
        try {
            Result result = userManagerService.addRule(rule);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 用于用户违规时， 进行封号
     */
    @RequestMapping("/closeAccount.do")
    @ResponseBody
    public Result closeAccount(@RequestBody Rule rule){
        try {
            Result result = userManagerService.closeAccount(rule);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 通过type字段或者条件查找相关的用户信息
     */
    @RequestMapping("/findUserByType.do")
    @ResponseBody
    public PageResult findUserByType(Integer type, @RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = userManagerService.findUserByTypr(type, queryPageBean);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(MessageConstant.EXCEPTION_MESSAGE,false);
        }
    }

    /*
     * 根据用户status查询用户相关的信息
     */
    @RequestMapping("/findUserByStatus.do")
    @ResponseBody
    public PageResult findUserByStatus(Integer status, @RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = userManagerService.findUserByStatus(status, queryPageBean);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(MessageConstant.EXCEPTION_MESSAGE,false);
        }
    }

    /*
     * 解除禁言
     */
    @RequestMapping("/lift.do")
    @ResponseBody
    public Result lift(Integer id){
        try {
            Result result = userManagerService.lift(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

}
