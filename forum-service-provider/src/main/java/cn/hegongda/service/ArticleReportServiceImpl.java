package cn.hegongda.service;

import cn.hegongda.constant.MessageConstant;
import cn.hegongda.mapper.TArticleCategoryMapper;
import cn.hegongda.mapper.TArticleMapper;
import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.article.ArticleReportService;
import cn.hegongda.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(interfaceClass = ArticleReportService.class)
public class ArticleReportServiceImpl implements ArticleReportService {


    @Autowired
    private TArticleMapper articleMapper;

    @Autowired
    private TArticleCategoryMapper tArticleCategoryMapper;

    /*
     * 获取每日的阅读量
     */
    @Override
    public PageResult getDayTotal(Integer id, QueryPageBean queryPageBean) {
        if (id == null){
            return new PageResult("系统出错",false);
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<Map> list = articleMapper.getDayTotal(id);
        PageInfo info = new PageInfo(list);
        return new PageResult(info.getTotal(),list,"查询成功",true);
    }


    /*
     *  按照时间段进行查询
     */
    @Override
    public PageResult searchByTime(Integer id, QueryPageBean queryPageBean) {
        // 对参数进行判断
        if (id == null || queryPageBean == null){
            return new PageResult("请按照正常方式进行操作",false);
        }
        // 前端没有传入时间则查询全部
        if (queryPageBean.getTimeArray() == null || queryPageBean.getTimeArray().length == 0){
            return getDayTotal(id,queryPageBean);
        }
        String beginTime = DateUtils.format(queryPageBean.getTimeArray()[0]);
        String endTime = DateUtils.format(queryPageBean.getTimeArray()[1]);
        // 封装查询条件
        Map<String, String> map = new HashMap<>();
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        map.put("uid",id+"");
        // 查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<Map> list = articleMapper.searchByTime(map);
        PageInfo info = new PageInfo(list);
        return new PageResult(info.getTotal(),list,"操作成功",true);
    }

    /*
     * 查询昨天文章分类信息
     */
    @Override
    public Result showYearsDay(Integer id) {
        // 判断参数
        if (id == null){
            return new Result(false, "此操作为非法操作");
        }
        // 用map构造查询条件
        Map<String, String> map = new HashMap<>();
        map.put("uid",id+"");
        map.put("date",DateUtils.getYesterday());

        // 1、查询数据再  2、查数据将图例封装
        List<String> tips = new ArrayList<>();
        List<Map> list = articleMapper.getYesterDay(map);
        // 当无数据时构造数据
        if (list == null || list.size() == 0){
            Map<String,String> newMap = new HashMap<>();
            newMap.put("name","无阅读量");
            newMap.put("value","0");
            list.add(newMap);
            tips.add("无阅读量");
            return new Result(true,"操作成功",list,tips);
        }
        // 获取图例
        for (Map map1 : list) {
            Set<String> temp = map1.keySet();
            for (String key : temp) {
                tips.add(map1.get(key)+"");
                break;
            }
        }
        System.out.println(list.size());
        return new Result(true,"操作成功",list,tips);
    }


    /*
     * 查询上周阅读量
     */
    @Override
    public Result getLastWeek(Integer id) {
        if(id == null){
            return new Result(false,"此操作为非法操作");
        }
        // 获取上周日的时间
         Date sunday = DateUtils.getLastWeekSunday(new Date());
         String endTime = DateUtils.format(sunday);

         //封装条件
         Map<String,String> map = new HashMap<>();
         map.put("endTime",endTime);
         map.put("uid",id+"");
        // 获取数据
        List<Integer> list = articleMapper.getLastWeekNumber(map);
        System.out.println("ahha");
        return new Result(true,"操作成功",list);
    }

    /*
     * 获取前十二个月每个月的阅读
     */
    @Override
    public Result getPreMonthsTotal(Integer id) {
        // 获取上月第一天
        Date beginTime = DateUtils.getFirstDay4LastMonth();
        // 获取上月的最后一天(本月1号)
        Date endTime = DateUtils.getFirstDay4ThisMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        List<String> monthList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        while (!DateUtils.format(calendar.getTime()).equals(DateUtils.format(endTime))){
            String datetime = DateUtils.format(calendar.getTime());
            Integer number = articleMapper.getDayNumbers(id,datetime);
            numberList.add(number==null?0:number);
            String day = datetime.substring(datetime.lastIndexOf("-") + 1);
            monthList.add(day);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("months",monthList);
        map.put("number",numberList);
        return new Result(true,"查询成功",map);
    }

    /*
     * 后台管理人员查询昨天、上周、上月发文总数
     */
    @Override
    public Result getArticleNumber() {
        List<Integer> list = new ArrayList<>();
        // 获取昨天日期
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String dateStr = DateUtils.format(calendar.getTime());
        Integer dnumber= articleMapper.getYearsToday(dateStr);
        list.add(dnumber == null ? 0 : dnumber);

        // 获取上周发文总数
        String monday = DateUtils.format(DateUtils.geLastWeekMonday(date));
        String sunday = DateUtils.format(DateUtils.getLastWeekSunday(date));
        Integer wnumber = articleMapper.getLastWeekArticleNumber(monday, sunday);
        list.add(wnumber == null ? 0 : wnumber);

        // 获取上月发文总数
        String month = DateUtils.format(DateUtils.getFirstDay4LastMonth());
        month = month.substring(0,7);
        Integer mnumber = articleMapper.getLastMonthArticleNumber(month);
        list.add(mnumber == null ? 0 : mnumber);
        return new Result(true, MessageConstant.OPERATION_SUCCESS, list);
    }

    /*
     * 获取上月个一级分类发文数量
     */

    @Override
    public Result getLastMonthCategoryNumber() {
        // 获取上月
        String month = DateUtils.format(DateUtils.getFirstDay4LastMonth()).substring(0,7);
        // 查询一级分类
        List<TArticleCategory> categories = tArticleCategoryMapper.findAll();
        Map<String, Object> map = new HashMap<>();
        List<String> cateList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (TArticleCategory category : categories) {
            cateList.add(category.getCname());
            Integer number = articleMapper.getLastMonthCategoryArticleNumber(month,category.getId());
            numberList.add(number == null ? 0 : number);
        }

        map.put("count",numberList);
        map.put("category",cateList);
        return new Result(true, MessageConstant.OPERATION_SUCCESS, map);
    }

    /*
     * 获取上周发文情况
     */
    @Override
    public Result getLastWeekArticleNumber() {
        Date date = new Date();
        List<Integer> days = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.geLastWeekMonday(date));
        Map<String, Object> map = new HashMap<>();
        for(int i = 0; i < 7; i++){
            days.add(i + 1);
            String day = DateUtils.format(calendar.getTime());
            Integer number = articleMapper.getYearsToday(day);
            numbers.add(number == null ? 0 : number);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        map.put("days",days);
        map.put("numbers", numbers);
        return new Result(true, MessageConstant.OPERATION_SUCCESS, map);
    }

    /*
     * 技术欢迎程度排名
     */

    @Override
    public Result getPopularTechnology() {
        String month = DateUtils.format(DateUtils.getFirstDay4LastMonth()).substring(0,7);
        List<Map> maps =  articleMapper.getPopularTechonolgy(month);
        List<String> cateList = new ArrayList<>();
        List<Long> counList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        for (Map mp : maps) {
            Long total = (Long) mp.get("total");
            counList.add(total);
            Integer cid = (Integer) mp.get("cid");
            TArticleCategory category = tArticleCategoryMapper.getCategoryNameById(cid);
            cateList.add(category.getCname());
        }
        map.put("category", cateList);
        map.put("count",counList);
        return new Result( true, MessageConstant.OPERATION_SUCCESS, map);
    }
}
