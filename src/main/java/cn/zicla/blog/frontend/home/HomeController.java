package cn.zicla.blog.frontend.home;

import cn.zicla.blog.frontend.base.FrontendBaseController;
import cn.zicla.blog.rest.article.Article;
import cn.zicla.blog.rest.article.ArticleService;
import cn.zicla.blog.rest.base.Pager;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/home")
public class HomeController extends FrontendBaseController {


    @Autowired
    ArticleService articleService;

    /**
     * 首页
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/index")
    public String index(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "15") Integer pageSize
    ) {

        //准备文章分页了。
        Pager<Article> articlePager = articleService.page(
                page,
                pageSize,
                Sort.Direction.DESC,
                Sort.Direction.DESC,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        model.addAttribute("articlePager", articlePager);

        return "home/index";
    }


    /**
     * 关于我们
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/about")
    public String about(Model model, HttpServletRequest request, HttpServletResponse response) {

        return "home/about";
    }


}
