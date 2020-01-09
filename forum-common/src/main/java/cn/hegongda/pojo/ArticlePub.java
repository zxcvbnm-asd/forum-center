package cn.hegongda.pojo;

import java.io.Serializable;
import java.util.Date;

public class ArticlePub implements Serializable{

    private Integer id;
    private Integer num;
    private String labels;
    private String coverUrl;
    private String pubTime;
    private String title;
    private Integer cid;
    private Integer uid;
    private String username;
    private String cname;
    private Integer parentId;
    private Date timestamp;   // 用于对logstash的判断

    public ArticlePub(Integer id, Integer num, String labels, String coverUrl, String pubTime, String title, Integer cid, Integer uid, String username, String cname) {
        this.id = id;
        this.num = num;
        this.labels = labels;
        this.coverUrl = coverUrl;
        this.pubTime = pubTime;
        this.title = title;
        this.cid = cid;
        this.uid = uid;
        this.username = username;
        this.cname = cname;
    }

    public ArticlePub() {
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}


