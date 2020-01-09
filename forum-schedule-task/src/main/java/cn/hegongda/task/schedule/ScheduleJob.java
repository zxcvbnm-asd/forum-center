package cn.hegongda.task.schedule;

import cn.hegongda.constant.RedisConstant;
import cn.hegongda.result.Result;
import cn.hegongda.service.ArticleService;
import cn.hegongda.utils.QiniuUtils;
import cn.hegongda.utils.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;

public class ScheduleJob {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private ArticleService articleService;

    /**
     *  将上传至云服务器与本地数据库中的图片进行比较，删除多余图片
     */
    public void differPicture(){
        Jedis jedis = jedisPool.getResource();
        // 通过diff进行比较
        Set<String> set = jedis.sdiff(RedisConstant.USER_PIC_DB_RESORCES, RedisConstant.USER_PIC_YUN_RESORCES);
        if (set != null || set.size() > 0){
            for (String s : set) {
                QiniuUtils.deleteFileFromQiniu(s);  // 删除多余的图片
            }
        }

        System.out.println("图片删除完毕");
    }

    /**
     *  每天夜里12点之前将每天各种分类的阅读量存入到数据库中
     */
    public void readRecoderToDatabase(){
        Jedis jedis = jedisPool.getResource();
        Map<String, String> map = jedis.hgetAll(RedisConstant.ARTICLE_READ_RECODER);
        // 遍历map
        if (map != null || map.size() > 0) {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                Integer total = Integer.valueOf(map.get(key));
                String[] ids = key.split("-");
                articleService.readRecoderToDatabase(total, ids[0], ids[1]);
                // 插入数据库后删除还该key  value
                jedis.hdel(RedisConstant.ARTICLE_READ_RECODER, key);
            }
        }
        System.out.println("阅读量已经统计完毕");
    }

    /**
     * 每天十二点将访问最多的文章从redis中清除，重新统计数据
     */
    public void clearRedisMaxNumArticle(){
        Jedis jedis = jedisPool.getResource();
        jedis.ltrim(RedisConstant.MAX_NUM_ARTICLE_LIST, 1, 0);
        System.out.println("门户文章清理完毕");
    }
}
