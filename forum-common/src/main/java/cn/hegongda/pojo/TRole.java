package cn.hegongda.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class TRole implements Serializable {
    private Integer id;

    private String roleName;

    private String keyword;

    private String description;

    private List<TPermission> permissions;

    public List<TPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<TPermission> permissions) {
        this.permissions = permissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}