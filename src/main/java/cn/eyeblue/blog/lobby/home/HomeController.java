package cn.eyeblue.blog.lobby.home;

import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.lobby.base.FrontendBaseController;
import cn.eyeblue.blog.rest.article.Article;
import cn.eyeblue.blog.rest.article.ArticleDao;
import cn.eyeblue.blog.rest.article.ArticleService;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.tag.Tag;
import cn.eyeblue.blog.rest.tag.TagService;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.rest.user.UserDao;
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

@Controller
public class HomeController extends FrontendBaseController {


    @Autowired
    ArticleDao articleDao;

    @Autowired
    ArticleService articleService;

    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;


    /**
     * 首页
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/home/index")
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
                0,
                10,
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
    @RequestMapping("/home/article/{uuid}")
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
     * 访问文章的快捷方式。比如可以通过 /a/zicla/2018-04-24 访问到 path为2018-04-24这一篇文章。
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/a/{username}/{path}")
    public String shortcutArticle(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String username,
            @PathVariable String path
    ) {

        //确认用户是否存在。
        User user = userDao.findTopByUsername(username);
        if (user == null) {
            throw new UtilException("访问地址错误。[user not found]");
        }

        Article article = articleDao.findTopByUserUuidAndPath(user.getUuid(), path);
        String ip = NetworkUtil.getIpAddress(request);
        articleService.wrapDetail(article, ip);

        model.addAttribute("article", article);

        return "home/article/detail";
    }


    /**
     * 用户详情
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/home/user/{userUuid}")
    public String userDetail(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String userUuid,
            @RequestParam(required = false) String tagUuid,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "15") Integer pageSize
    ) {

        User author = userService.detail(userUuid);

        model.addAttribute("author", author);


        //用户的文章分类。
        Pager<Tag> tagPager = tagService.page(
                0,
                50,
                Sort.Direction.DESC,
                userUuid,
                null);
        model.addAttribute("tagPager", tagPager);


        //准备文章分页了。
        Pager<Article> articlePager = articleService.page(
                page,
                pageSize,
                Sort.Direction.DESC,
                Sort.Direction.DESC,
                null,
                null,
                null,
                userUuid,
                false,
                null,
                tagUuid,
                null,
                null);

        //准备热门文章分页了。
        Pager<Article> hotArticlePager = articleService.page(
                0,
                10,
                null,
                null,
                Sort.Direction.DESC,
                null,
                null,
                userUuid,
                false,
                null,
                null,
                null,
                null);

        model.addAttribute("articlePager", articlePager);
        model.addAttribute("hotArticlePager", hotArticlePager);


        //准备tag详情
        if (tagUuid == null) {
            tagUuid = "";
        }
        model.addAttribute("tagUuid", tagUuid);

        return "home/user/detail";
    }


}
