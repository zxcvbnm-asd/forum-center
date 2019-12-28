package cn.hegongda.service;


import cn.hegongda.constant.MessageConstant;
import cn.hegongda.mapper.FanAttenMapper;
import cn.hegongda.result.Result;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = FanAttenService.class)
public class FanAttenServiceImpl implements FanAttenService {

    @Autowired
    private FanAttenMapper fanAttenMapper;

    // 判断用户跟文章作者是否为粉丝关系
    @Override
    public Result JudgeRelation(Integer fid, Integer tid) {
        //参数校验
        if(fid == null || tid == null){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }

        // 判断参数是否相同，如果相同，则说明读者正在看自己文章
        if(tid == fid){
            return new Result(false,"作者与当前用户为同一个人");
        }
        // 从数据库中查询
        Integer number = fanAttenMapper.isExists(fid,tid);
        if (number!= null && number > 0){
            return new Result(true,"已关注");
        }
        return new Result(false,"未关注");
    }


    /*
     * 用于关注和取消关注
     */
    @Override
    @Transactional
    public Result attenOrNot(Integer fid, Integer tid) {
        //参数校验
        if(fid == null || tid == null){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        // 判断参数是否相同，如果相同，则说明读者正在看自己文章
        if(tid == fid){
            return new Result(false,"作者与当前用户为同一个人");
        }
        // 从数据库中查询
        Integer number = fanAttenMapper.isExists(fid,tid);
        if (number!= null && number > 0){
            // 说明是取消关注
            fanAttenMapper.unsubscribe(fid,tid);
            return new Result(true,"已取消关注");
        }
        fanAttenMapper.subscribe(fid,tid);
        return new Result(true,"已关注");
    }


    /*
     * 查询作者的详细信息
     */
    @Override
    public Result getWriterDetail(Integer id) {
        if(id == null){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }

        Map map= fanAttenMapper.getWriterDetail(id);
        return new Result(true,MessageConstant.OPERATION_SUCCESS,map);
    }

    /*
    * 查询作者的书籍信息
    */
    @Override
    public Result getArticleList(Integer id) {
        if(id == null){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }

        List<Map> list= fanAttenMapper.getArticleList(id);
        for (Map map : list) {
            if (StringUtils.isBlank((String) map.get("coverUrl"))){
                map.put("coverUrl","11.png");
            }
        }
        return new Result(true,MessageConstant.OPERATION_SUCCESS,list);
    }


    /*
     * 查询作者关注
     */
    @Override
    public Result getUserAtten(Integer id) {
        if (id == null) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        List<Map> list = fanAttenMapper.getUserAtten(id);
        return new Result(true, MessageConstant.OPERATION_SUCCESS,list);
    }

    /*
     * 查询作者粉丝
     */
    @Override
    public Result getUserFan(Integer id) {
        if (id == null) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        List<Map> list = fanAttenMapper.getUserFan(id);
        return new Result(true, MessageConstant.OPERATION_SUCCESS,list);
    }
}
