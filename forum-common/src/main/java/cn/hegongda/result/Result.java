package cn.hegongda.result;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * 封装返回结果
 */
public class Result implements Serializable{
    private boolean flag;//执行结果，true为执行成功 false为执行失败
    private String message;//返回结果信息，主要用于页面提示信息
    private Object data;//返回数据
    private Object token;  // 专门用于回传token制定
    private List<String> tip;  // 用于封装饼状图的图例
    public Result(boolean flag, String message) {
        super();
        this.flag = flag;
        this.message = message;
    }

    public Result(boolean flag, String message, Object data) {
        this.flag = flag;
        this.message = message;
        this.data = data;
    }

    public Result(boolean flag, String message, Object data, Object token) {
        this.flag = flag;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public Result(boolean flag, String message, Object data, List<String> tip) {
        this.flag = flag;
        this.message = message;
        this.data = data;
        this.tip = tip;
    }

    public List<String> getTip() {
        return tip;
    }

    public void setTip(List<String> tip) {
        this.tip = tip;
    }

    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }
}
