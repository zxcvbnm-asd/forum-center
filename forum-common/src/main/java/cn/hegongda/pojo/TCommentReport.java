package cn.hegongda.pojo;

import java.io.Serializable;
import java.util.Date;

public class TCommentReport implements Serializable {
    private Integer id;

    private String category;

    private Long commentId;

    private Integer uid;

    private Date reportTime;

    private byte[] mark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
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

    public byte[] getMark() {
        return mark;
    }

    public void setMark(byte[] mark) {
        this.mark = mark;
    }
}