package cn.hegongda.mapper;

import cn.hegongda.pojo.TAdmin;
import cn.hegongda.pojo.TAdminExample;
import cn.hegongda.pojo.TAdminWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TAdminMapper {
    int countByExample(TAdminExample example);

    int deleteByExample(TAdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TAdminWithBLOBs record);

    int insertSelective(TAdminWithBLOBs record);

    List<TAdminWithBLOBs> selectByExampleWithBLOBs(TAdminExample example);

    List<TAdmin> selectByExample(TAdminExample example);

    TAdminWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TAdminWithBLOBs record, @Param("example") TAdminExample example);

    int updateByExampleWithBLOBs(@Param("record") TAdminWithBLOBs record, @Param("example") TAdminExample example);

    int updateByExample(@Param("record") TAdmin record, @Param("example") TAdminExample example);

    int updateByPrimaryKeySelective(TAdminWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TAdminWithBLOBs record);

    int updateByPrimaryKey(TAdmin record);
}