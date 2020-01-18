package cn.hegongda.mapper;

import cn.hegongda.pojo.Rule;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface RuleMapper {

    @Select("select * from t_rule where uid=#{uid}")
    Rule getRuleByUid(Integer uid);

    void updateRule(Rule rule);

    @Insert("insert into t_rule (begin_time,end_time,num,uid,mark,status) values " +
            "(#{beginTime},#{endTime},#{num},#{uid},#{mark},#{status})")
    void addRule(Rule rule);
}
