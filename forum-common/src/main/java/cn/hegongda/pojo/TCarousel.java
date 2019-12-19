package cn.hegongda.pojo;

import java.io.Serializable;
import java.util.Date;

public class TCarousel implements Serializable {
    private Integer id;

    private String imgUrl;

    private Date createTiime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public Date getCreateTiime() {
        return createTiime;
    }

    public void setCreateTiime(Date createTiime) {
        this.createTiime = createTiime;
    }
}