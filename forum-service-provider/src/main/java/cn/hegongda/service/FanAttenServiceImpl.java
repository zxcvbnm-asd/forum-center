package cn.hegongda.service;


import cn.hegongda.constant.MessageConstant;
import cn.hegongda.mapper.FanAttenMapper;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.utils.DateUtils;
import cn.hegongda.utils.JsonUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.server.LoaderHandler;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

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
        fanAttenMapper.subscribe(fid,tid,new Date());
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


    /*
     * 分页家条件查询用户的关注
     */
    @Override
    public PageResult getUserAttens(Integer id, QueryPageBean queryPageBean) {
        if ( id == null || queryPageBean == null ){
            return new PageResult( MessageConstant.PARAM_NULL_MESSAGE,false);
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<Map> mapList = fanAttenMapper.getUserAttens(id, queryPageBean.getQueryString());
        PageInfo info = new PageInfo(mapList);
        return new PageResult(info.getTotal(),mapList,MessageConstant.OPERATION_SUCCESS,true);
    }


    /*
     * 查询粉丝总数
     */
    @Override
    public Result getSumFans(Integer id) {
        if(id == null ){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        Integer total = fanAttenMapper.getSumFans(id);
        return new Result(true,MessageConstant.OPERATION_SUCCESS,total);
    }

    /*
     * 根据天数查询粉丝
     */
    @Override
    public Result getFansByDays(Integer id, Integer days) {
        if (id == null || days == null || days < 6) {
            return new Result(false,MessageConstant.PARAM_NULL_MESSAGE);
        }

        int addDays = get(days);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        Map<String, Object> map = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 7; i++){
            if (i == 1){
                calendar.add(Calendar.DAY_OF_YEAR, 0);
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, addDays);
            }
            String date = DateUtils.format(calendar.getTime());
            dates.add(date);
            Integer number = fanAttenMapper.getFansCountByDays(date,id);
            numbers.add(number);
        }
        map.put("dates", dates);
        map.put("numbers" ,numbers);
        return new Result(true, MessageConstant.OPERATION_SUCCESS, map);
    }


    private int  get(int days) {
        if (days == 6) {
            return  1;
        } else if (days == 30) {
            return  5;
        } else {
            return  15;
        }
    }

    /*
     * 通过时间段进行查询
     */
    @Override
    public Result getFansByTime(Integer id, QueryPageBean queryPageBean) {
        if (queryPageBean == null || queryPageBean.getTimeArray() == null || queryPageBean.getTimeArray().length < 1){
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        Calendar calendar = Calendar.getInstance();
        int days = getSubDays(queryPageBean,calendar) - 1;

        if ( days <= 6 ){
            return new Result(false, "输入的时间间隔不能小于7天");
        }

        int addDays = days / 5;
        int lastDays = days % 5;
        Map<String, Object> map = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        calendar.setTime(queryPageBean.getTimeArray()[0]);
        String date = null;
        for ( int i = 1; i <= 7; i++ ) {
            if (i == 1) {
                date = DateUtils.format(queryPageBean.getTimeArray()[0]);
            } else if (i == 7 ){
                date = DateUtils.format(queryPageBean.getTimeArray()[1]);
            } else {
                if (lastDays > 0) {
                    calendar.add(Calendar.DAY_OF_YEAR,addDays+1);
                    date = DateUtils.format(calendar.getTime());
                    lastDays--;
                }else {
                    calendar.add(Calendar.DAY_OF_YEAR,addDays);
                    date = DateUtils.format(calendar.getTime());
                }
            }

            dates.add(date);
            Integer number = fanAttenMapper.getFansCountByDays(date, id);
            numbers.add(number);
        }
        map.put("dates",dates);
        map.put("numbers",numbers);
        return new Result(true, MessageConstant.OPERATION_SUCCESS, map);
    }

    // 查询两个日期相差天数
    private int getSubDays(QueryPageBean queryPageBean , Calendar calendar){
        calendar.setTime(queryPageBean.getTimeArray()[0]);
        LocalDate date1 = LocalDate.of(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.DAY_OF_MONTH));
        calendar.setTime(queryPageBean.getTimeArray()[1]);
        LocalDate date2 = LocalDate.of(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH ) + 1,calendar.get(Calendar.DAY_OF_MONTH));
        int days = Math.abs(Period.between(date1, date2).getDays());
        return days;
    }

}
