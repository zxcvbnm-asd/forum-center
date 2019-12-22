package cn.hegongda.controller.article;


import cn.hegongda.pojo.TArticle;
import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.result.Result;
import cn.hegongda.service.ArticleService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.CacheRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Reference
    private ArticleService articleService;

    // 查询一级分类
    @RequestMapping("/findFirstCate.do")
    @ResponseBody
    public Result findFirstCategoey(){
        try{
            List<TArticleCategory> list = articleService.findFirstCategory();
            return new Result(true, "查询成功", list);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,"系统繁忙");
        }
    }

    // 查询二级分类
    @RequestMapping("/findSecondCate.do")
    @ResponseBody
    public Result findSecondCategoey(Integer parentId){
        try{
            List<TArticleCategory> list = articleService.findSecondCategory(parentId);
            return new Result(true, "查询成功", list);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,"系统繁忙");
        }
    }

    // 文章发布
    @RequestMapping(value = "/publish.do" , method = RequestMethod.POST)
    @ResponseBody
    public Result publish(@RequestBody TArticle article){
        // 进行参数判断
        if ( article == null ){
            return new Result(false,"请按照正常方式发布文章");
        }
        try {
            Result result = articleService.pubArticle(article);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "系统出现问题，请稍后发布");
        }
    }

    //saveDraft
    @RequestMapping(value = "/saveDraft.do" , method = RequestMethod.POST)
    @ResponseBody
    public Result saveDraft(@RequestBody TArticle article){
        // 进行参数判断
        try {
            Result result = articleService.saveDraft(article);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "系统繁忙，请稍后再试");
        }
    }


    @RequestMapping("/schedulePub.do")
    @ResponseBody
    public Result schedulePub(@RequestBody TArticle article){
        System.out.println(article.getPubTime());
        return new Result(false,"成功");
    }
}
