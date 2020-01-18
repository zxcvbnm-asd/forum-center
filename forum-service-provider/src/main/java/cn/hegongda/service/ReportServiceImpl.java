package cn.hegongda.service;


import cn.hegongda.constant.MessageConstant;
import cn.hegongda.mapper.ReportMapper;
import cn.hegongda.mapper.TUserMapper;
import cn.hegongda.pojo.Report;
import cn.hegongda.pojo.TUser;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.report.ReportService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private TUserMapper userMapper ;

    /*
     *  添加举报信息
     */
    @Override
    @Transactional
    public Result addReport(Report report) {
        if (report == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        Integer number = reportMapper.addReport(report);
        if (number > 0) {
            return new Result(true, MessageConstant.OPERATION_SUCCESS);
        }
        return new Result(false, MessageConstant.OPERATION_FAIL);
    }

    /**
     * 根据状态和条件进行查询举报信息
     * 当条件存储
     *
     *
     * @param status
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult getReport(Integer status, Integer cate, QueryPageBean queryPageBean) {
        if (status == null || cate == null || queryPageBean == null ) {
            return new PageResult( MessageConstant.PARAM_NULL_MESSAGE, false);
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<Map> list =  reportMapper.getReport(status, cate,queryPageBean.getQueryString());
        PageInfo info = new PageInfo(list) ;
        return new PageResult(info.getTotal(), list, MessageConstant.OPERATION_SUCCESS, true);
    }


    /*
     * 修改举报信息状态
     */
    @Override
    @Transactional
    public Result changeStatus(Integer id) {
        if (id == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        Report report = reportMapper.getReportById(id);
        if (report != null && report.getStatus() != 1){
            report.setStatus(1);
            reportMapper.updateRepoert(report);
            return new Result(true, MessageConstant.OPERATION_SUCCESS );
        }
        return new Result( false, MessageConstant.OPERATION_FAIL);
    }

    /*
     * 查看被举报的评论
     */

    @Override
    public PageResult getCommentReport(Integer status, Integer cate, QueryPageBean queryPageBean) {
        if (status == null || cate == null || queryPageBean == null ) {
            return new PageResult( MessageConstant.PARAM_NULL_MESSAGE, false);
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<Map> mapList = reportMapper.getCommentReport(status,cate);
        for (Map map : mapList) {
            Integer pid = (Integer) map.get("pid");
            TUser user = userMapper.selectByPrimaryKey(pid);
            map.put("publisher", user.getUsername());
        }
        PageInfo info = new PageInfo(mapList);
        return new PageResult(info.getTotal(), mapList, MessageConstant.OPERATION_SUCCESS, true);
    }
}
