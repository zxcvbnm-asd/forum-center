package cn.hegongda.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.mapper.TAdminMapper;
import cn.hegongda.mapper.TPermissionMapper;
import cn.hegongda.mapper.TRoleMapper;
import cn.hegongda.pojo.AdminUserInfo;
import cn.hegongda.pojo.TAdmin;
import cn.hegongda.pojo.TPermission;
import cn.hegongda.pojo.TRole;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.admin.AdminUserService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.MacSpi;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(interfaceClass = AdminUserService.class)
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private TAdminMapper adminMapper;

    @Autowired
    private TRoleMapper roleMapper;

    @Autowired
    private TPermissionMapper permissionMapper;


    /*
     * 查询后台管理员信息
     */
    @Override
    public PageResult findAdminUser(QueryPageBean queryPageBean) {
        if (queryPageBean == null ) {
            return new PageResult(MessageConstant.OPERATION_FAIL,false);
        }

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<TAdmin> list = adminMapper.findAdminUser(queryPageBean.getQueryString());
        PageInfo info = new PageInfo(list);
        return new PageResult(info.getTotal(), list, MessageConstant.OPERATION_SUCCESS, true);
    }

    /*
     * 删除用户（即将其状态改变为1）即可
     */
    @Override
    @Transactional
    public Result deleteAdminUserById(Integer id) {
       if ( id == null ) {
           return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
       }
       // 删除外user-role表中记录
        adminMapper.deleteUserRole(id);
        // 删除user中记录
        adminMapper.deleteAdminUserById(id);
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 改变管理者的状态
     */
    @Override
    @Transactional
    public Result changeStatus(Integer id) {
        if (id == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        // 先查再改
        TAdmin admin = adminMapper.findAdminUserById(id);
        if (admin != null ) {
            if (admin.getStatus() == 1){
                admin.setStatus(0);
            } else {
                admin.setStatus(1);
            }
            adminMapper.update(admin);
            return new Result(true, MessageConstant.OPERATION_SUCCESS);
        }
        return new Result(false, MessageConstant.OPERATION_FAIL);
    }


    /*
     * 获取用户角色权限
     */
    @Override
    public Result getAdminUserInfoById(Integer id) {
        if (id == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        int i = 0;
        // 查询用户
        AdminUserInfo adminUserInfo = adminMapper.findUserInfo(id);
        adminUserInfo.setLevel(1);
        adminUserInfo.setTitle("用户信息");
        adminUserInfo.setIds(i++);

        // 查询角色
        List<AdminUserInfo> children = adminMapper.findUserRoleById(id);
        if (children != null && children.size() > 0) {
            for (AdminUserInfo child : children) {
                child.setLevel(2);
                child.setTitle("角色信息");
                child.setIds(i++);
                child.setRid(adminUserInfo.getId());
                // 查询权限
                List<AdminUserInfo> roleChildren = adminMapper.findUserPermission(child.getId());
                if (roleChildren != null && roleChildren.size() > 0) {
                    for (AdminUserInfo roleChild : roleChildren) {
                        roleChild.setLevel(3);
                        roleChild.setTitle("权限信息");
                        roleChild.setIds(i++);
                        roleChild.setRid(child.getId());
                    }
                    child.setChildren(roleChildren);
                }
            }
            adminUserInfo.setChildren(children);
        }
        return new Result(true, MessageConstant.OPERATION_SUCCESS, adminUserInfo);
    }

    /*
     * 删除管理人员拥有的角色或者权限
     */
    @Override
    @Transactional
    public Result deleteAdminAuthority(Integer id, Integer level, Integer rid) {
        if (id == null || level == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        if (level == 3) {
            // 删除permission
            adminMapper.deletePermission(id,rid);
        } else if (level == 2) {
            // 删除角色
            adminMapper.deleteRole(id,rid);
        } else if (level == 1) {
            return new Result(false, "不能删除用户");
        }
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 获取管理人员没有的角色
     */
    @Override
    public Result getAdminRole(Integer id) {
        if (id == null) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        List<TRole> list = adminMapper.getAdminRole(id);
        return new Result(true, MessageConstant.OPERATION_SUCCESS, list);
    }

    /*
     *  为用户添加角色
     */

    @Override
    @Transactional
    public Result addRoleForAdmin(Integer id, Integer[] ids) {
        if (id == null || ids == null || ids.length < 1) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        for (Integer role_id : ids) {
            adminMapper.insertAdminRole(id,role_id);
        }
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 查找角色
     */
    @Override
    public PageResult findRole(QueryPageBean queryPageBean) {
        if(queryPageBean == null) {
            return new PageResult( MessageConstant.PARAM_NULL_MESSAGE, false);
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<TRole> roles = adminMapper.findRole(queryPageBean.getQueryString());
        PageInfo info = new PageInfo(roles);
        return new PageResult(info.getTotal(), roles, MessageConstant.OPERATION_SUCCESS, true);
    }

    /*
     * 添加角色
     */
    @Override
    @Transactional
    public Result addRole(TRole role) {
        if(role == null) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        roleMapper.insert(role);
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 添加用户
     */
    @Override
    @Transactional
    public Result addAdminUser(TAdmin admin) {
        if (admin == null) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        admin.setCreateTime(new Date());
        adminMapper.insert(admin);
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    // 根据id查询角色
    @Override
    public Result findRoleById(Integer id) {
        if (id == null) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        TRole role = roleMapper.selectByPrimaryKey(id);
        return new Result(true, MessageConstant.OPERATION_SUCCESS, role);
    }

    /*
     * 获取某一角色下的权限
     */
    @Override
    public Result getPermissionByRid(Integer id) {
        if (id == null) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        List<TPermission> list = roleMapper.getPermissions(id);
        return new Result(true, MessageConstant.OPERATION_SUCCESS, list);
    }

    /*
     * 删除角色与权限表之间记录
     */
    @Override
    @Transactional
    public Result delRolePermission(Integer rid, Integer pid) {
        if (rid == null || pid == null ){
            return new Result(false, MessageConstant.OPERATION_FAIL);
        }
        roleMapper.delRolePermission(rid, pid);
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 查询某一角色下的权限
     */

    @Override
    public Result getPermissionOfRole(Integer id) {
        if (id == null ){
            return new Result(false, MessageConstant.OPERATION_FAIL);
        }
        List<TPermission> permissions = adminMapper.findPermissionOfRole(id);
        return new Result(true, MessageConstant.OPERATION_SUCCESS, permissions);
    }

    /*
     * 为角色添加权限
     */
    @Override
    @Transactional
    public Result addPermissionOfRole(Integer id, Integer[] ids) {
        if (id == null || ids == null || ids.length < 1) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        for (Integer permissionId : ids) {
            roleMapper.addPermission(id, permissionId);
        }
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 查询所有的权限，按照条件进行分页查询
     */

    @Override
    public PageResult getAllPermission(QueryPageBean queryPageBean) {
        if (queryPageBean == null ) {
            return new PageResult(MessageConstant.PARAM_NULL_MESSAGE, false);
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<TPermission> list = permissionMapper.findAllPermission(queryPageBean.getQueryString());
        PageInfo info = new PageInfo(list);
        return new PageResult(info.getTotal(), list, MessageConstant.OPERATION_SUCCESS, true);
    }

    /*
     * 删除权限
     */
    @Override
    public Result deletePermission(Integer id) {
        if (id == null) {
            return new Result(true, MessageConstant.PARAM_NULL_MESSAGE);
        }
        // 先删除role_permission 中间表中的记录
        permissionMapper.deleteRolePermission(id);
        // 删除Permission表中记录
        permissionMapper.deleteByPrimaryKey(id);
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }

    /*
     * 新增权限
     */
    @Override
    @Transactional
    public Result addPermission(TPermission permission) {
        if (permission == null) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        permissionMapper.insert(permission);
        return new Result(true, MessageConstant.OPERATION_SUCCESS);
    }
}
