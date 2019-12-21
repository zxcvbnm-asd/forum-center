package cn.hegongda.service;

import cn.hegongda.mapper.TArticleCategoryMapper;
import cn.hegongda.mapper.TArticleMapper;
import cn.hegongda.pojo.TArticle;
import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.result.Result;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Override
    public Result pubArticle(TArticle article) {
        return null;
    }
}
