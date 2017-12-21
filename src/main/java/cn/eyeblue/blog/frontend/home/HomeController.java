package cn.eyeblue.blog.frontend.home;

import cn.eyeblue.blog.frontend.base.FrontendBaseController;
import cn.eyeblue.blog.rest.article.Article;
import cn.eyeblue.blog.rest.article.ArticleService;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.tag.Tag;
import cn.eyeblue.blog.rest.tag.TagService;
import cn.eyeblue.blog.util.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/home")
public class HomeController extends FrontendBaseController {


    @Autowired
    ArticleService articleService;

    @Autowired
    TagService tagService;

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
                false,
                null,
                null,
                null,
                null);

        //准备热门文章分页了。
        Pager<Article> hotArticlePager = articleService.page(
                page,
                pageSize,
                null,
                null,
                Sort.Direction.DESC,
                null,
                null,
                null,
                false,
                null,
                null,
                null,
                null);

        model.addAttribute("articlePager", articlePager);
        model.addAttribute("hotArticlePager", hotArticlePager);

        return "home/index";
    }


    /**
     * 文章详情
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/article/{uuid}")
    public String articleDetail(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String uuid
    ) {

        String ip = NetworkUtil.getIpAddress(request);

        Article article = articleService.detail(uuid, ip);
        model.addAttribute("article", article);

        return "home/article/detail";
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
