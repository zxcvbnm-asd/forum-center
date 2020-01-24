package cn.hegongda.mapper;

import cn.hegongda.pojo.TPermission;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface TPermissionMapper {


    int deleteByPrimaryKey(Integer id);

    int insert(TPermission record);

    int insertSelective(TPermission record);



    TPermission selectByPrimaryKey(Integer id);



    int updateByPrimaryKeySelective(TPermission record);

    int updateByPrimaryKey(TPermission record);

    List<TPermission> findAllPermission(@Param("queryString") String queryString);

    @Delete("delete from t_role_permission where permission_id=#{id}")
    void deleteRolePermission(Integer id);
}