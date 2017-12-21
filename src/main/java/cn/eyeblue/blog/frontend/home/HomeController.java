package cn.eyeblue.blog.frontend.home;

import cn.eyeblue.blog.frontend.base.FrontendBaseController;
import cn.eyeblue.blog.rest.article.Article;
import cn.eyeblue.blog.rest.article.ArticleDao;
import cn.eyeblue.blog.rest.article.ArticleService;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.tag.Tag;
import cn.eyeblue.blog.rest.tag.TagService;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.rest.user.UserService;
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
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/home")
public class HomeController extends FrontendBaseController {


    @Autowired
    ArticleDao articleDao;

    @Autowired
    ArticleService articleService;

    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

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
     * 用户详情
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/user/{uuid}")
    public String userDetail(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String uuid,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "15") Integer pageSize
    ) {

        //准备用户详情。
        User author = userService.check(uuid);

        Object obj = articleDao.analysisTotal(author.getUuid());
        Object[] list = (Object[]) obj;
        author.setArticleNum(articleDao.countByUserUuidAndPrivacyFalseAndDeletedFalse(uuid));
        author.setArticleAgreeNum((Long) list[0]);
        author.setArticleWords((Long) list[1]);
        author.setArticleHit((Long) list[2]);
        author.setCommentNum((Long) list[3]);

        model.addAttribute("author", author);

        //准备文章分页了。
        Pager<Article> articlePager = articleService.page(
                page,
                pageSize,
                Sort.Direction.DESC,
                Sort.Direction.DESC,
                null,
                null,
                null,
                uuid,
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
                uuid,
                false,
                null,
                null,
                null,
                null);

        model.addAttribute("articlePager", articlePager);
        model.addAttribute("hotArticlePager", hotArticlePager);

        return "home/user/detail";
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
