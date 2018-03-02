package cn.eyeblue.blog.rest.report;

import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.article.Article;
import cn.eyeblue.blog.rest.article.ArticleDao;
import cn.eyeblue.blog.rest.article.ArticleService;
import cn.eyeblue.blog.rest.base.BaseEntityController;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.comment.Comment;
import cn.eyeblue.blog.rest.comment.CommentDao;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.histroy.HistoryDao;
import cn.eyeblue.blog.rest.support.captcha.SupportCaptchaService;
import cn.eyeblue.blog.rest.support.session.SupportSessionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/report")
public class ReportController extends BaseEntityController<Report, ReportForm> {

    @Autowired
    ReportService reportService;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    ReportDao reportDao;

    @Autowired
    ArticleService articleService;

    @Autowired
    HistoryDao historyDao;


    @Autowired
    CommentDao commentDao;


    @Autowired
    SupportSessionDao supportSessionDao;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SupportCaptchaService supportCaptchaService;


    public ReportController() {
        super(Report.class);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult del(@PathVariable String uuid) {
        Report report = this.check(uuid);

        reportDao.delete(report);

        return success();
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult detail(@PathVariable String uuid) {
        return super.detail(uuid);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult sort(@RequestParam String uuid1, @RequestParam Long sort1, @RequestParam String uuid2, @RequestParam Long sort2) {
        return super.sort(uuid1, sort1, uuid2, sort2);
    }

    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/page")
    public WebResult page(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Sort.Direction orderSort,
            @RequestParam(required = false) String entityUuid,
            @RequestParam(required = false) String entityName,
            @RequestParam(required = false) Report.Type type,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String ip,
            //是否需要entity的详情。
            @RequestParam(required = false, defaultValue = "true") Boolean needEntityDetail
    ) {

        Pager<Report> pager = reportService.page(
                page,
                pageSize,
                orderSort,
                entityUuid,
                entityName,
                type,
                content,
                ip
        );

        if (needEntityDetail) {
            pager.getData().forEach(report -> {
                if (report.getType() == Report.Type.REPORT_COMMENT) {
                    Optional<Comment> optionalComment = commentDao.findById(report.getEntityUuid());
                    if (optionalComment.isPresent()) {
                        Comment comment = optionalComment.get();
                        Optional<Article> optionalArticle = articleDao.findById(comment.getArticleUuid());

                        if (optionalArticle.isPresent()) {
                            Article article = optionalArticle.get();
                            comment.setArticleTitle(article.getTitle());
                        }
                        report.setComment(comment);
                    }
                } else if (report.getType() == Report.Type.REPORT_ARTICLE) {
                    Optional<Article> optionalArticle = articleDao.findById(report.getEntityUuid());
                    if (optionalArticle.isPresent()) {
                        report.setArticle(optionalArticle.get());
                    } else {
                        throw new UtilException("不存在啊~");
                    }

                }
            });
        }

        return this.success(pager);
    }

}
