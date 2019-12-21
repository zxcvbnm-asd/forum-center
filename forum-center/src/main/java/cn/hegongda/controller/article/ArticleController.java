package cn.hegongda.controller.article;


import cn.hegongda.pojo.TArticleCategory;
import cn.hegongda.result.Result;
import cn.hegongda.service.ArticleService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
