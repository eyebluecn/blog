package cn.zicla.blog.rest.article;

import cn.zicla.blog.config.exception.UtilException;
import cn.zicla.blog.rest.agree.History;
import cn.zicla.blog.rest.agree.HistoryDao;
import cn.zicla.blog.rest.base.BaseEntityController;
import cn.zicla.blog.rest.base.Pager;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.comment.Comment;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.captcha.SupportCaptchaService;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import cn.zicla.blog.rest.tank.TankService;
import cn.zicla.blog.rest.user.UserService;
import cn.zicla.blog.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/article")
public class ArticleController extends BaseEntityController<Article, ArticleForm> {

    @Autowired
    ArticleService articleService;

    @Autowired
    TankService tankService;

    @Autowired
    UserService userService;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    HistoryDao historyDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SupportCaptchaService supportCaptchaService;


    public ArticleController() {
        super(Article.class);
    }


    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult create(@Valid ArticleForm form) {
        return super.create(form);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult del(@PathVariable String uuid) {
        return super.del(uuid);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult edit(@Valid ArticleForm form) {
        return super.edit(form);
    }


    @Override
    @Feature(FeatureType.PUBLIC)
    public WebResult detail(@PathVariable String uuid) {
        Article article = this.check(uuid);
        article.setPosterTank(tankService.find(article.getPosterTankUuid()));
        article.setUser(userService.find(article.getUserUuid()));

        //统计文章点击数量。
        articleService.analysisHit(article, this.getCurrentRequestIp());


        return success(article);

    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult sort(@RequestParam String uuid1, @RequestParam Long sort1, @RequestParam String uuid2, @RequestParam Long sort2) {
        return super.sort(uuid1, sort1, uuid2, sort2);
    }


    @Feature(FeatureType.USER_MANAGE)
    @RequestMapping("/page")
    public WebResult page(

            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Sort.Direction orderSort,
            @RequestParam(required = false) Sort.Direction orderTop,
            @RequestParam(required = false) Sort.Direction orderHit,
            @RequestParam(required = false) Sort.Direction orderPrivacy,
            @RequestParam(required = false) Sort.Direction orderReleaseTime,
            @RequestParam(required = false) String userUuid,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String keyword
    ) {

        Pager<Article> articlePager = articleService.page(page,
                pageSize,
                orderSort,
                orderTop,
                orderHit,
                orderPrivacy,
                orderReleaseTime,
                userUuid,
                title,
                tag,
                keyword);

        return this.success(articlePager);

    }


    //给某篇文章点赞。
    @Feature(FeatureType.PUBLIC)
    public WebResult agree(
            @RequestParam String articleUuid) {

        Article article = this.check(articleUuid);

        String ip = getCurrentRequestIp();
        int count = historyDao.countByTypeAndEntityUuidAndIp(History.Type.AGREE_ARTICLE, articleUuid, ip);
        if (count > 0) {
            throw new UtilException("请勿重复点赞！");
        }

        History history = new History();
        history.setType(History.Type.AGREE_ARTICLE);
        history.setEntityUuid(articleUuid);
        history.setIp(ip);
        historyDao.save(history);


        article.setAgree(article.getAgree() + 1);
        articleDao.save(article);

        return success("点赞成功!");
    }

    //取消点赞。
    @Feature(FeatureType.PUBLIC)
    public WebResult cancelAgree(@RequestParam String articleUuid) {

        Article article = this.check(articleUuid);


        String ip = getCurrentRequestIp();
        History history = historyDao.findTopByTypeAndEntityUuidAndIp(History.Type.AGREE_ARTICLE, articleUuid, ip);
        if (history == null) {
            throw new UtilException("您没有点赞过这篇文章，操作失败！");
        }

        historyDao.delete(history);
        article.setAgree(article.getAgree() - 1);
        articleDao.save(article);

        return success("取消点赞成功!");
    }

}
