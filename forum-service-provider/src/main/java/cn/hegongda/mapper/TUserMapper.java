package cn.hegongda.mapper;

import cn.hegongda.pojo.TUser;
import cn.hegongda.pojo.TUserExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TUserMapper {
    int countByExample(TUserExample example);

    int deleteByExample(TUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TUser record);

    int insertSelective(TUser record);

    List<TUser> selectByExample(TUserExample example);

    TUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TUser record, @Param("example") TUserExample example);

    int updateByExample(@Param("record") TUser record, @Param("example") TUserExample example);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    @Select("select count(*) from t_user where username=#{username}")
    int findCountUserByUserName(String username);

    @Select("select count(*) from t_user where mobile=#{mobile}")
    int findCountUserByUserMobile(String mobile);

    public TUser findUserByUsername(String username);

    TUser findUserByMobile(String mobile);

    @Select("select * from t_user where username=#{username}")
    TUser findByUsername(String  username);


    List<Map> findUserByType(@Param("type") Integer type, @Param("queryString") String queryString);

    List<Map> findUserByStatus(@Param("status") Integer status, @Param("queryString") String queryString);
}