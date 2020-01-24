package cn.hegongda.mapper;

import cn.hegongda.pojo.TPermission;
import cn.hegongda.pojo.TRole;
import cn.hegongda.pojo.TRoleExample;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TRoleMapper {
    int countByExample(TRoleExample example);

    int deleteByExample(TRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TRole record);

    int insertSelective(TRole record);

    List<TRole> selectByExample(TRoleExample example);

    TRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TRole record, @Param("example") TRoleExample example);

    int updateByExample(@Param("record") TRole record, @Param("example") TRoleExample example);

    int updateByPrimaryKeySelective(TRole record);

    int updateByPrimaryKey(TRole record);

    @Select("SELECT id as id,permission_name as permissionName,keyword FROM t_permission WHERE id IN (SELECT permission_id FROM t_role_permission WHERE role_id=#{id})")
    List<TPermission> getPermissions(Integer id);

    @Delete("delete from t_role_permission where role_id=#{rid} and permission_id=#{pid}")
    void delRolePermission(@Param("rid") Integer rid, @Param("pid") Integer pid);

    @Insert("insert into t_role_permission (role_id, permission_id) values (#{id},#{permissionId})")
    void addPermission(@Param("id") Integer id, @Param("permissionId") Integer permissionId);

}