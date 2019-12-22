package cn.hegongda.service;

import cn.hegongda.mapper.TArticleCategoryMapper;
import cn.hegongda.mapper.TArticleMapper;
import cn.hegongda.pojo.TArticle;
import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.result.Result;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = ArticleService.class)
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private TArticleMapper articleMapper;

    @Autowired
    private TArticleCategoryMapper articleCategoryMapper;


    // 查询出所有的一级分类
    @Override
    public List<TArticleCategory> findFirstCategory() {
        List<TArticleCategory> list = articleCategoryMapper.findAll();
        return list;
    }

    // 根据以及分类查询子类
    @Override
    public List<TArticleCategory>findSecondCategory(Integer parentId) {

        List<TArticleCategory> list = articleCategoryMapper.findChildByParentId(parentId);
        return list;
    }

    // 发布文章
    @Override
    @Transactional
    public Result pubArticle(TArticle article) {
        // 设置相关参数
        Date pubTime = new Date();
        article.setPubTime(pubTime);
        article.setStatus(2);  // 已经发布
        int number = articleMapper.insert(article);
        if (number > 0){
            return new Result(true, "发布成功");
        }
        return new Result(false, "发布失败");
    }


    @Override
    @Transactional
    public Result saveDraft(TArticle article) {
        if(article == null){
            return new Result(false, "请按照正常方式操作");
        }
        // 设置相关参数
        Date pubTime = new Date();
        article.setPubTime(pubTime);
        article.setStatus(3);  // 存入草稿箱
        int number = articleMapper.insert(article);
        if (number > 0){
            return new Result(true, "成功存入草稿箱");
        }
        return new Result(false, "存入失败");
    }
}
