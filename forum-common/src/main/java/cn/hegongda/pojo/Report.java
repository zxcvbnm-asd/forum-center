package cn.hegongda.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 *  举报的实体类
 */
public class Report implements Serializable{

    private  Integer id;
    private String content;   // 内容
    private Integer type;      // 举报种类
    private Date reportTime;   // 举报时间
    private Integer status;   // 举报状态（是否已经处理）
    private Integer cate;
    private Integer cid;      // 举报内容 0  文章举报  1 评论举报 2 话题举报
    private Integer uid;      // 举报人的信息

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getCate() {
        return cate;
    }

    public void setCate(Integer cate) {
        this.cate = cate;
    }
}
