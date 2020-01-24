package cn.hegongda.pojo;

import java.io.Serializable;
import java.util.List;

public class AdminUserInfo implements Serializable {

    private Integer level;
    private String roleName;
    private Integer id;
    private String title;
    private String keyword;
    private Integer ids;
    private Integer rid;
    private List<AdminUserInfo> children;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getIds() {
        return ids;
    }

    public void setIds(Integer ids) {
        this.ids = ids;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<AdminUserInfo> getChildren() {
        return children;
    }

    public void setChildren(List<AdminUserInfo> children) {
        this.children = children;
    }
}
