package cn.hegongda.pojo;

import java.io.Serializable;

public class TArticleCategoryWithBLOBs extends TArticleCategory implements Serializable {
    private byte[] cname;

    private byte[] rmark;

    public byte[] getCname() {
        return cname;
    }

    public void setCname(byte[] cname) {
        this.cname = cname;
    }

    public byte[] getRmark() {
        return rmark;
    }

    public void setRmark(byte[] rmark) {
        this.rmark = rmark;
    }
}