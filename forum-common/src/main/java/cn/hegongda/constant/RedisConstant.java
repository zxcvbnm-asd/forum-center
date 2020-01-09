package cn.hegongda.constant;

public class RedisConstant {
    // 已经上传到数据库中的图片
    public static final String USER_PIC_DB_RESORCES = "USER_PIC_DB_RESORCES";

    // 已经上传到云端的图片
    public static final String USER_PIC_YUN_RESORCES = "USER_PIC_YUN_RESORCES";

    // redis保存用户登陆信息的
    public static final String USER_TOKEN = "USER_TOKEN";

    // 获取随机验证码的前缀
    public static final String VALIDATE_CODE = "VALIDATE_CODE";

    // 根据Id查询出用户信息后，保存至redis中，避免每次都访问数据库
    public static final String USER_INFORMATION = "USER_INFORMATION";

    // 将审核状态的文章放入到redis中，当后台管理员审核时直接在redis中获取即可
    public static final String CHECK_ARTICLE = "CHECK_ARTICLE";

    // 将门户页面查询出的最大文章分类存在redis中
    public static final String MAX_NUM_ARTICLE_LIST = "MAX_NUM_ARTICLE_LIST";

    // 文章点赞数前缀
    public static final String SUPPOT_NUM_ARTICLE = "SUPPOT_NUM_ARTICLE";

    // 文章当天阅读数统计前缀
    public static final String ARTICLE_READ_RECODER = "ARTICLE_READ_RECODER";
}
