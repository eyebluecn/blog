package cn.eyeblue.blog.rest.article;

import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.BaseEntityController;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.histroy.History;
import cn.eyeblue.blog.rest.histroy.HistoryDao;
import cn.eyeblue.blog.rest.report.ReportService;
import cn.eyeblue.blog.rest.support.captcha.SupportCaptchaService;
import cn.eyeblue.blog.rest.support.session.SupportSessionDao;
import cn.eyeblue.blog.rest.tank.TankService;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.rest.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    ReportService reportService;

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
    @Feature(FeatureType.USER_MINE)
    public WebResult create(@Valid ArticleForm form) {
        return super.create(form);
    }

    @Override
    @Feature(FeatureType.USER_MINE)
    public WebResult del(@PathVariable String uuid) {

        //判断文章是否是自己的方可删除。
        Article article = this.check(uuid);

        //验证权限
        checkMineEntityPermission(FeatureType.USER_MANAGE, FeatureType.USER_MINE, article.getUserUuid());

        //举报了自己的report统统设置为已处理。
        reportService.softDelete(uuid);

        return super.del(uuid);
    }

    @Override
    @Feature(FeatureType.USER_MINE)
    public WebResult edit(@Valid ArticleForm form) {

        Article article = this.check(form.getUuid());
        //验证权限
        checkMineEntityPermission(FeatureType.USER_MANAGE, FeatureType.USER_MINE, article.getUserUuid());

        return super.edit(form);
    }


    @Override
    @Feature(FeatureType.PUBLIC)
    public WebResult detail(@PathVariable String uuid) {

        return success(articleService.detail(uuid, getCurrentRequestIp()));

    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult sort(@RequestParam String uuid1, @RequestParam Long sort1, @RequestParam String uuid2, @RequestParam Long sort2) {
        return super.sort(uuid1, sort1, uuid2, sort2);
    }

    /**
     *
     * @param page 【选填】当前页码 0基，默认0
     * @param pageSize 【选填】每页的大小，默认30
     * @param orderSort 【选填】是否按Sort进行排序，ASC升序，DESC降序
     * @param orderTop 【选填】是否按置顶情况进行排序，ASC升序，DESC降序
     * @param orderHit 【选填】是否按点击数量进行排序，ASC升序，DESC降序
     * @param orderPrivacy 【选填】是否按私有性进行排序，ASC升序，DESC降序
     * @param orderCreateTime 【选填】是否按创建时间进行排序，ASC升序，DESC降序
     * @param userUuid 【选填】作者uuid
     * @param privacy 【选填】私有性
     * @param title 【选填】标题，左右模糊匹配
     * @param tag 【选填】标签，左右模糊匹配
     * @param keyword 【选填】关键字，左右模糊匹配
     * @return 文章分页信息
     */
    @Feature(FeatureType.USER_MINE)
    @RequestMapping("/page")
    public WebResult page(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Sort.Direction orderSort,
            @RequestParam(required = false) Sort.Direction orderTop,
            @RequestParam(required = false) Sort.Direction orderHit,
            @RequestParam(required = false) Sort.Direction orderPrivacy,
            @RequestParam(required = false) Sort.Direction orderCreateTime,
            @RequestParam(required = false) String userUuid,
            @RequestParam(required = false) Boolean privacy,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String keyword
    ) {

        User operator = checkUser();

        Pager<Article> articlePager = articleService.page(page,
                pageSize,
                orderSort,
                orderTop,
                orderHit,
                orderPrivacy,
                orderCreateTime,
                userUuid,
                privacy,
                title,
                tag,
                keyword,
                operator);

        return this.success(articlePager);

    }


    /**
     * 给某篇文章点赞
     * @param articleUuid 文章
     * @return 点赞结果
     */
    @RequestMapping("/agree")
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
        history.setEntityUuid(articleUuid);
        history.setEntityName(article.getTitle());
        history.setType(History.Type.AGREE_ARTICLE);
        history.setIp(ip);
        historyDao.save(history);


        article.setAgree(article.getAgree() + 1);
        articleDao.save(article);

        return success("点赞成功!");
    }

    /**
     * 取消点赞
     * @param articleUuid 文章
     * @return 取消点赞结果
     */
    @RequestMapping("/cancel/agree")
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


    /**
     * 置顶某篇文章
     * @param articleUuid 文章
     * @return 置顶结果
     */
    @RequestMapping("/top")
    @Feature(FeatureType.USER_MANAGE)
    public WebResult top(
            @RequestParam String articleUuid) {

        Article article = this.check(articleUuid);

        if (article.getTop()) {
            throw new UtilException("文章已经处于置顶状态，请勿重复操作。");
        }

        article.setTop(true);

        articleDao.save(article);

        return success("置顶成功!");
    }

    /**
     * 取消点赞。
     * @param articleUuid 文章
     * @return 结果
     */
    @RequestMapping("/cancel/top")
    @Feature(FeatureType.USER_MANAGE)
    public WebResult cancelTop(@RequestParam String articleUuid) {

        Article article = this.check(articleUuid);


        if (!article.getTop()) {
            throw new UtilException("文章未处于置顶状态，请勿重复操作。");
        }

        article.setTop(false);

        articleDao.save(article);

        return success("取消置顶成功!");
    }


}
