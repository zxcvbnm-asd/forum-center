package cn.hegongda.controller;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.pojo.TAdmin;
import cn.hegongda.pojo.TPermission;
import cn.hegongda.pojo.TRole;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.admin.AdminUserService;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebResult;

/**
 *  后台管理人员控制器
 */
@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Reference
    private AdminUserService adminUserService;

    /*
     * 查询后台管理员信息
     */
    @RequestMapping("/findAdminUser.do")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('USER_ROLE','IS_AUTHENTICATED_ANONYMOUSLY')")
    public PageResult findAdminUser(@RequestBody QueryPageBean queryPageBean){
        try {
            return adminUserService.findAdminUser(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(MessageConstant.EXCEPTION_MESSAGE,false);
        }
    }

    /*
     * 删除用户（即将其状态改变为1）即可
     */
    @RequestMapping("/changeStatus.do")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_USER','IS_AUTHENTICATED_ANONYMOUSLY')")
    public Result changeStatus(Integer id){
        try {
            return adminUserService.changeStatus(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 删除用户（即将其状态改变为1）即可
     */
    @RequestMapping("/deleteAdminUserById.do")
    @ResponseBody
    public Result deleteAdminUserById(Integer id){
        try {
            return adminUserService.deleteAdminUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 查询用户具体的角色和权限
     */
    @RequestMapping("/getAdminUserInfoById.do")
    @ResponseBody
    public Result getAdminUserInfoById(Integer id){
        try {
            return adminUserService.getAdminUserInfoById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 删除管理人员拥有的角色或者权限（id 和 rid 确定中间表中唯一条记录）
     */
    @RequestMapping("/deleteAdminAuthority.do")
    @ResponseBody
    public Result deleteAdminAuthority(Integer id, Integer level, Integer rid){
        try {
            return adminUserService.deleteAdminAuthority(id, level, rid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 获取管理人员没有的角色
     */
    @RequestMapping("/getAdminRole.do")
    @ResponseBody
    public Result getAdminRole(Integer id){
        try {
            return adminUserService.getAdminRole(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 为用户添加角色
     */
    @RequestMapping("/addRoleForAdmin.do")
    @ResponseBody
    public Result addRoleForAdmin(Integer id, @RequestBody  Integer []ids){
        try {
            return adminUserService.addRoleForAdmin(id,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 查询所有角色
     */
    @RequestMapping("/findRole.do")
    @ResponseBody
    public PageResult findRole(@RequestBody QueryPageBean queryPageBean){
        try {
            return adminUserService.findRole(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(MessageConstant.EXCEPTION_MESSAGE,false);
        }
    }

    /*
     * addRole 添加角色
     */
    @RequestMapping("/addRole.do")
    @ResponseBody
    public Result addRole(@RequestBody TRole role){
        try {
            return adminUserService.addRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
    * addAdminUser 添加管理员
    */
    @RequestMapping("/addAdminUser.do")
    @ResponseBody
    public Result addAdminUser(@RequestBody TAdmin admin){
        try {
            return adminUserService.addAdminUser(admin);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 根据id查询角色
     */
    @RequestMapping("/findRoleById.do")
    @ResponseBody
    public Result findRoleById(Integer id) {
        try {
            return adminUserService.findRoleById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 查询某一角色下的所有权限
     */
    @RequestMapping("/getPermissionByRid.do")
    @ResponseBody
    public Result getPermissionByRid(Integer id){
        try {
            return adminUserService.getPermissionByRid(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 删除角色与权限表之间记录
     */
    @RequestMapping("/delRolePermission.do")
    @ResponseBody
    public Result delRolePermission(Integer rid, Integer pid){
        try {
            return adminUserService.delRolePermission(rid, pid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 查询某一角色下的权限
     */
    @RequestMapping("/getPermissionOfRole.do")
    @ResponseBody
    public Result getPermissionOfRole(Integer id){
        try {
            return adminUserService.getPermissionOfRole(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 为角色添加权限
     */
    @RequestMapping("/addPermissionOfRole.do")
    @ResponseBody
    public Result addPermissionOfRole(Integer id, @RequestBody Integer []ids){
        try {
            return adminUserService.addPermissionOfRole(id, ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.OPERATION_FAIL) ;
        }
    }

    /*
     * 查询所有的权限
     */
    @RequestMapping("/getAllPermission.do")
    @ResponseBody
    public PageResult getAllPermission(@RequestBody QueryPageBean queryPageBean){
        try {
            return adminUserService.getAllPermission(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(MessageConstant.EXCEPTION_MESSAGE, false);
        }
    }

    /*
     * 删除权限
     */
    @RequestMapping("/deletePermission.do")
    @ResponseBody
    public Result deletePermission(Integer id) {
        try {
            return adminUserService.deletePermission(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

    /*
     * 新增权限
     */
    @RequestMapping("/addPermission.do")
    @ResponseBody
    public Result addPermission(@RequestBody TPermission permission) {
        try {
            return adminUserService.addPermission(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EXCEPTION_MESSAGE);
        }
    }

}
