package cn.hegongda.service;

import cn.hegongda.mapper.TArticleMapper;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ArticleReportService.class)
public class ArticleReportServiceImpl implements ArticleReportService {


    @Autowired
    private TArticleMapper articleMapper;

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
}
