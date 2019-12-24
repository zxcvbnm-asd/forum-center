package cn.hegongda.result;

import java.io.Serializable;
import java.util.Date;

/**
 * 封装查询条件
 */
public class QueryPageBean implements Serializable{
    private Integer currentPage;//页码
    private Integer pageSize;//每页记录数
    private String queryString;//查询条件
    private Date []timeArray;  // 时间范围进行封装


    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Date[] getTimeArray() {
        return timeArray;
    }

    public void setTimeArray(Date[] timeArray) {
        this.timeArray = timeArray;
    }
}