package cn.eyeblue.blog.rest.article;

import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.comment.Comment;
import cn.eyeblue.blog.rest.comment.CommentDao;
import cn.eyeblue.blog.rest.common.MailService;
import cn.eyeblue.blog.rest.common.NotificationResult;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.histroy.History;
import cn.eyeblue.blog.rest.histroy.HistoryDao;
import cn.eyeblue.blog.rest.support.session.SupportSessionDao;
import cn.eyeblue.blog.rest.tag.TagService;
import cn.eyeblue.blog.rest.tank.TankService;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.rest.user.UserService;
import cn.eyeblue.blog.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Slf4j
@Service
public class ArticleService extends BaseEntityService<Article> {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    HistoryDao historyDao;

    @Autowired
    UserService userService;

    @Autowired
    TankService tankService;

    @Autowired
    TagService tagService;

    @Autowired
    CommentDao commentDao;

    @Autowired
    MailService mailService;

    @Autowired
    SupportSessionDao supportSessionDao;

    public ArticleService() {
        super(Article.class);
    }

    public Pager<Article> page(
            Integer page,
            Integer pageSize,
            Sort.Direction orderSort,
            Sort.Direction orderTop,
            Sort.Direction orderHit,
            Sort.Direction orderPrivacy,
            Sort.Direction orderCreateTime,
            String userUuid,
            Boolean privacy,
            String title,
            String tag,
            String keyword,
            User operator
    ) {

        //验证权限。超级管理员可以查看所有人的私有文章。普通用户只能查看自己的私有文章。
        if (privacy == null || privacy) {
            if (operator == null) {
                //没有权限却想查看别人，那么就强制不给看。
                privacy = false;
            } else if (!operator.hasPermission(FeatureType.USER_MANAGE)) {
                if (userUuid == null || !userUuid.equals(operator.getUuid())) {
                    //没有权限却想查看别人，那么就强制不给看。
                    privacy = false;
                }
            }
        }


        Sort sort = new Sort(Sort.Direction.ASC, Article_.deleted.getName());

        //最先看top的排序，然后再是其他排序依据。
        if (orderTop != null) {
            sort = sort.and(new Sort(orderTop, Article_.top.getName()));
        }

        if (orderSort != null) {
            sort = sort.and(new Sort(orderSort, Article_.sort.getName()));
        }


        if (orderHit != null) {
            sort = sort.and(new Sort(orderHit, Article_.hit.getName()));
        }

        if (orderPrivacy != null) {
            sort = sort.and(new Sort(orderPrivacy, Article_.privacy.getName()));
        }

        if (orderCreateTime != null) {
            sort = sort.and(new Sort(orderCreateTime, Article_.createTime.getName()));
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);


        Boolean finalPrivacy = privacy;
        Page<Article> pageData = getDao().findAll(((root, query, cb) -> {
            Predicate predicate = cb.equal(root.get(Article_.deleted), false);

            if (userUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Article_.userUuid), userUuid));
            }
            if (title != null) {
                predicate = cb.and(predicate, cb.like(root.get(Article_.title), "%" + title + "%"));
            }
            if (finalPrivacy != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Article_.privacy), finalPrivacy));
            }
            if (tag != null) {
                predicate = cb.and(predicate, cb.like(root.get(Article_.tags), "%" + tag + "%"));
            }

            if (keyword != null) {

                Predicate predicate1 = cb.like(root.get(Article_.title), "%" + keyword + "%");
                Predicate predicate2 = cb.like(root.get(Article_.html), "%" + keyword + "%");

                predicate = cb.and(predicate, cb.or(predicate1, predicate2));
            }
            return predicate;

        }), pageable);

        long totalItems = pageData.getTotalElements();
        List<Article> list = pageData.getContent();


        list.forEach(article -> {
            //作者
            article.setUser(userService.find(article.getUserUuid()));
            //标签装点
            article.setTagArray(tagService.getTagsByUuids(JsonUtil.toStringList(article.getTags())));
        });

        return new Pager<>(page, pageSize, totalItems, list);
    }


    //获取一篇文章的详情。
    public Article detail(String uuid, String ip) {


        Article article = this.check(uuid);

        article.setPosterTank(tankService.find(article.getPosterTankUuid()));
        article.setUser(userService.find(article.getUserUuid()));

        //装点标签
        article.setTagArray(tagService.getTagsByUuids(JsonUtil.toStringList(article.getTags())));

        //统计文章点击数量。
        this.analysisHit(article, ip);

        //查看当前用户的点赞与否情况。
        History history = historyDao.findTopByTypeAndEntityUuidAndIp(History.Type.AGREE_ARTICLE, uuid, ip);
        if (history != null) {
            article.setAgreed(true);
        }
        return article;
    }


    //统计访问数量。
    @Async
    public void analysisHit(Article article, String ip) {


        int count = historyDao.countByTypeAndEntityUuidAndIp(History.Type.VISIT_ARTICLE, article.getUuid(), ip);
        if (count > 0) {
            //之前就已经访问过了，忽略。
            return;
        }

        History history = new History();
        history.setEntityUuid(article.getUuid());
        history.setEntityName(article.getTitle());
        history.setType(History.Type.VISIT_ARTICLE);
        history.setIp(ip);
        historyDao.save(history);

        article.setHit(article.getHit() + 1);
        articleDao.save(article);

    }

    //给文章作者发邮件。
    @Async
    public void emailComment(Article article, Comment comment, String host) {

        User user = userService.check(article.getUserUuid());

        //如果评论者是自己，那么不通知邮件。
        if (article.getUserUuid().equals(comment.getUserUuid())) {
            return;
        }
        //如果文章不接受评论或者用户邮箱没有通过验证，则不发送。
        if (!article.getNeedNotify()) {
            return;
        }
        if (!user.getEmailValidate()) {
            return;
        }
        
        String url = "http://" + host + "/home/article/" + article.getUuid();
        String html = "<p>文章:《" + article.getTitle() + "》</p><p>用户：" + comment.getName() + "</p><p>邮箱：" + comment.getEmail() + "</p><p>内容：" + comment.getContent() + "\"</p><p><a href=\"" + url + "\">点击查看</a></p>";
        NotificationResult notificationResult = mailService.htmlSend(user.getEmail(), "《" + article.getTitle() + "》收到新评论了", html);

        if (notificationResult.getStatus() != NotificationResult.Status.OK) {
            log.warn(html);
        }
    }


}
