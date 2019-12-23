package cn.hegongda.result;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装对象
 */
public class PageResult implements Serializable{
    private Long total;//总记录数
    private List rows;//当前页结果
    private String message; // 提示信息
    private boolean flag;

    public PageResult(Long total, List rows, String message, boolean flag) {
        this.total = total;
        this.rows = rows;
        this.message = message;
        this.flag = flag;
    }

    public PageResult(String message, boolean flag) {
        this.message = message;
        this.flag = flag;
    }

    public PageResult(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public List getRows() {
        return rows;
    }
    public void setRows(List rows) {
        this.rows = rows;
    }
}
