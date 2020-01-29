package cn.hegongda.mapper;

import cn.hegongda.pojo.AdminUserInfo;
import cn.hegongda.pojo.TAdmin;

import java.util.List;

import cn.hegongda.pojo.TPermission;
import cn.hegongda.pojo.TRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TAdminMapper {

    List<TAdmin> findAdminUser(@Param("queryString") String queryString);

    TAdmin findAdminUserById(@Param("id") Integer id);

    void update(TAdmin admin);

    void deleteUserRole(Integer id);

    @Delete("delete from t_admin where id=#{id}")
    void deleteAdminUserById(Integer id);

    @Select("select id,username as roleName from t_admin where id=#{id}")
    AdminUserInfo findUserInfo(Integer id);

    @Select("SELECT  id, role_name AS roleName , keyword FROM t_role  \n" +
            "WHERE id IN (SELECT role_id FROM t_admin_role WHERE admin_id=#{id})")
    List<AdminUserInfo> findUserRoleById(Integer id);

    @Select("SELECT  id, permission_name AS roleName , keyword FROM t_permission  \n" +
            "WHERE id IN (SELECT permission_id FROM t_role_permission WHERE role_id=#{id})")
    List<AdminUserInfo> findUserPermission(Integer id);

    @Delete("delete from t_role_permission where permission_id=#{id} and role_id=#{rid}")
    void deletePermission(@Param("id") Integer id, @Param("rid") Integer rid);

    @Delete("delete from t_admin_role where role_id=#{id} and admin_id=#{rid}")
    void deleteRole(@Param("id") Integer id, @Param("rid") Integer rid);

    @Select("select role_name as roleName, id as id, keyword, description\n" +
            "from t_role\n" +
            "where id not in (select role_id from t_admin_role where admin_id=#{id})")
    List<TRole> getAdminRole(Integer id);

    @Select("select role_name as roleName, id as id, keyword, description\n" +
            "from t_role\n" +
            "where id in (select role_id from t_admin_role where admin_id=#{id})")
    List<TRole> getAdminOfRole(Integer id);

    @Insert("insert into t_admin_role (admin_id, role_id) values (#{id},#{role_id})")
    void insertAdminRole(@Param("id") Integer id, @Param("role_id") Integer role_id);

    List<TRole> findRole(@Param("queryString") String queryString);

    void insert(TAdmin admin);

    @Select("SELECT id as id, permission_name as permissionName,keyword  FROM t_permission " +
            "WHERE id NOT IN " +
            "(SELECT permission_id FROM t_role_permission WHERE role_id=#{id})")
    List<TPermission> findPermissionOfRole(Integer id);

    TAdmin findAdminUserByName(String username);
}