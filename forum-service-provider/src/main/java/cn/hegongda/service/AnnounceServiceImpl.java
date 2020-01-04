package cn.hegongda.service;


import cn.hegongda.constant.MessageConstant;
import cn.hegongda.mapper.NoticeMapper;
import cn.hegongda.mapper.TAnnounceMapper;
import cn.hegongda.pojo.TAnnounce;
import cn.hegongda.pojo.TNotice;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = AnnounceService.class)
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    private TAnnounceMapper announceMapper ;

    @Autowired
    private NoticeMapper noticeMapper;


    /*
     * 分页查询公告
     */
    @Override
    public PageResult getByPages(Integer id, QueryPageBean queryPageBean) {
        if(id == null || queryPageBean == null ){
            return new PageResult(MessageConstant.PARAM_NULL_MESSAGE , false) ;
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 先从公告表中分页查询出相应评论，在到中间表中看用户是否已经读取评论
        List<TAnnounce> list = announceMapper.getAnnouncesByPage();
        for (TAnnounce tAnnounce : list) {
            Integer status = announceMapper.getStatus(id, tAnnounce.getId());
            // 没有读取则status为 0
            if (status == null) status = 0;
            tAnnounce.setStatus(status);
        }
        PageInfo info = new PageInfo(list) ;
        return new PageResult(info.getTotal(), list, MessageConstant.OPERATION_SUCCESS, true);
    }


    /*
     * 公告用户已经读了，设置为 status 1
     */
    @Override
    public Result changeStatus(Integer uid, Integer aid) {
        if (uid == null || aid == null ){
            return new Result( false , MessageConstant.PARAM_NULL_MESSAGE );
        }
        // 查询数据库中是否存在记录
        Integer count = announceMapper.existsUserAnnouce( uid, aid);
        // 如果不存在，直接插入导数据中即可
        if ( count < 1 ) {
            announceMapper.changeStatus( uid , aid );
        }
        // 如果存在，则返回成功操作即可
        return new Result( true , MessageConstant.OPERATION_SUCCESS);
    }


    /*
     * 分页查询用户通知
     */
    @Override
    public PageResult getNotices(Integer id, QueryPageBean queryPageBean) {
        if(id == null || queryPageBean == null ){
            return new PageResult(MessageConstant.PARAM_NULL_MESSAGE , false) ;
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize()) ;
        List<TNotice> list = noticeMapper.getNotices(id);
        PageInfo info = new PageInfo(list) ;
        return new PageResult(info.getTotal() , list, MessageConstant.OPERATION_SUCCESS, true);
    }

    /*
     * changeNoticeStatus （0 未读， 1 已读）
     */

    @Override
    @Transactional
    public Result changeNoticeStatus(Integer id) {
        if ( id == null ) {
            return new Result(false, MessageConstant.PARAM_NULL_MESSAGE);
        }
        TNotice notice = noticeMapper.getNoticeById(id);
        if (notice != null ) {
            notice.setStatus(1);
            noticeMapper.updateNotice(notice);
            return new Result( true , MessageConstant.OPERATION_SUCCESS);
        }
        return new Result(false , MessageConstant.OPERATION_FAIL);
    }
}
