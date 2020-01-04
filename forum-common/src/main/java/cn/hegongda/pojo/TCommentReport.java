package cn.hegongda.pojo;

import java.io.Serializable;
import java.util.Date;

public class TCommentReport implements Serializable {
    private Integer id;

    private Integer type;  // 1 内容涉黄 2 政治相关 3 内容抄袭 4 涉嫌广告  5 内容侵权  6 侮辱谩骂  7 其他方面

    private Long commentId;

    private Integer uid;

    private Date reportTime;

    private String mark;

    private Integer status;   // 用户标识用户是否已读

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}