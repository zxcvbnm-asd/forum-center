package cn.hegongda.pojo;

import java.util.List;

public class CommentExpan extends TComment {

    private String customerName;  // 评论人信息
    private String replyCustomerName;   // 回复人信息
    private List<CommentExpan> replyComments;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReplyCustomerName() {
        return replyCustomerName;
    }

    public void setReplyCustomerName(String replyCustomerName) {
        this.replyCustomerName = replyCustomerName;
    }

    public List<CommentExpan> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<CommentExpan> replyComments) {
        this.replyComments = replyComments;
    }
}
