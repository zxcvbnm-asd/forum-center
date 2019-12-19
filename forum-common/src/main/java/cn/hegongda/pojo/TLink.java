package cn.hegongda.pojo;

import java.io.Serializable;

public class TLink implements Serializable {
    private Integer id;

    private String linkName;

    private String linkHref;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName == null ? null : linkName.trim();
    }

    public String getLinkHref() {
        return linkHref;
    }

    public void setLinkHref(String linkHref) {
        this.linkHref = linkHref == null ? null : linkHref.trim();
    }
}