package cn.hegongda.service.admin;

import cn.hegongda.pojo.TAdmin;
import cn.hegongda.pojo.TPermission;
import cn.hegongda.pojo.TRole;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;

public interface AdminUserService {
    //查询后台管理员信息
    PageResult findAdminUser(QueryPageBean queryPageBean);

    // 改变其状态
    Result changeStatus(Integer id);

    // 删除用户（即将其状态改变为1）即可
    Result deleteAdminUserById(Integer id);

    // 获取用户角色权限信息
    Result getAdminUserInfoById(Integer id);

    //删除管理人员拥有的角色或者权限
    Result deleteAdminAuthority(Integer id, Integer level, Integer rid);

    //获取管理人员没有的角色
    Result getAdminRole(Integer id);

    //  为用户添加角色
    Result addRoleForAdmin(Integer id, Integer[] ids);

    // 查找角色
    PageResult findRole(QueryPageBean queryPageBean);

    // 添加角色
    Result addRole(TRole role);

    // 添加用户
    Result addAdminUser(TAdmin admin);

    //根据id查询角色
    Result findRoleById(Integer id);

    // 查询某一角色下的所有权限
    Result getPermissionByRid(Integer id);

    // 删除角色与权限表之间记录
    Result delRolePermission(Integer rid, Integer pid);

    // 查询某一角色下的权限
    Result getPermissionOfRole(Integer id);

    // 为角色添加权限
    Result addPermissionOfRole(Integer id, Integer[] ids);

    // 查询所有的权限
    PageResult getAllPermission(QueryPageBean queryPageBean);

    // 删除权限
    Result deletePermission(Integer id);

    // 新增权限
    Result addPermission(TPermission permission);
}
