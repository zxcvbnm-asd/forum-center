package cn.hegongda.controller.article;


import cn.hegongda.pojo.TArticle;
import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.result.PageResult;
import cn.hegongda.result.QueryPageBean;
import cn.hegongda.result.Result;
import cn.hegongda.service.ArticleService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
            // 判断status的参数，参数不为空说明是草稿箱中发布，为空则是直接发布
            if (article.getStatus() == null){
                // 设置相关参数
                Date pubTime = new Date();
                article.setPubTime(pubTime);
                article.setStatus(1);  // 当文章发布立刻变成审核状态
            }
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

    // 定时发布任务
    @RequestMapping("/schedulePub.do")
    @ResponseBody
    public Result schedulePub(@RequestBody TArticle article){
        try {
            return articleService.schedulePub(article);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false, "系统出现异常");
        }

    }

    // 查询全部文章
    @RequestMapping("/findAllByPage.do")
    @ResponseBody
    public PageResult findAllByPage(@RequestBody QueryPageBean queryPageBean){
         try {
             PageResult pageResult = articleService.findAllByPage(queryPageBean);
             return pageResult;
         } catch (Exception e){
             e.printStackTrace();
             return new PageResult("查询出错",false);
         }
    }

    // 根据id查询文章
    @RequestMapping("/findById.do")
    @ResponseBody
    public Result findById(Integer id){
        try {
            Result result = articleService.findById(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "系统繁忙");
        }
    }


    // 根据id删除文章
    @RequestMapping("/deleteById.do")
    @ResponseBody
    public Result deleteById(Integer id){
        try {
            Result result = articleService.deleteById(id);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "系统繁忙，请稍后重试");
        }
    }
}
