package cn.hegongda.mapper;

import cn.hegongda.pojo.TUser;
import cn.hegongda.pojo.TUserExample;
import java.util.List;
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

    @Select("select * from t_user where username=#{username}")
    public TUser findUserBuUsername(String username);

    @Select("select * from t_user where mobile=#{mobile}")
    TUser findUserByMobile(String mobile);
}