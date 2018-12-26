package cn.eyeblue.blog.rest.article;

import cn.eyeblue.blog.config.exception.BadRequestException;
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
import cn.eyeblue.blog.util.NetworkUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            Sort.Direction orderUpdateTime,
            Sort.Direction orderCreateTime,
            Sort.Direction orderTop,
            Sort.Direction orderHit,
            Sort.Direction orderPrivacy,
            String userUuid,
            Boolean privacy,
            String title,
            String tag,
            String keyword,
            List<ArticleType> types,
            String documentUuid,
            User operator,
            boolean needTags
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


        Sort sort = null;

        //最先看top的排序，然后再是其他排序依据。
        if (orderTop != null) {
            sort = this.and(sort, new Sort(orderTop, Article_.top.getName()));
        }

        //因为置顶的排序放到第一位
        sort = this.and(sort, defaultSort(orderSort, orderUpdateTime, orderCreateTime));


        if (orderHit != null) {
            sort = this.and(sort, new Sort(orderHit, Article_.hit.getName()));
        }

        if (orderPrivacy != null) {
            sort = this.and(sort, new Sort(orderPrivacy, Article_.privacy.getName()));
        }


        Pageable pageable = getPageRequest(page, pageSize, sort);


        Boolean finalPrivacy = privacy;
        Page<Article> pageData = getDao().findAll(((root, query, cb) -> {
            Predicate predicate = cb.isNotNull(root.get(Article_.uuid));

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

            if (CollectionUtils.isNotEmpty(types)) {
                CriteriaBuilder.In<ArticleType> typeIn = cb.in(root.get(Article_.type));
                types.forEach(typeIn::value);
                predicate = cb.and(predicate, typeIn);
            }

            if (documentUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Article_.documentUuid), documentUuid));
            }

            return predicate;

        }), pageable);

        long totalItems = pageData.getTotalElements();
        List<Article> list = pageData.getContent();


        if (needTags) {
            list.forEach(article -> {
                User author = userService.find(article.getUserUuid());
                //作者
                article.setUser(author);
                //标签装点
                article.setTagArray(tagService.getTagsByUuids(JsonUtil.toStringList(article.getTags())));
                //访问链接
                article.loadVisitUrlAndPath(null, author);
            });
        }


        return new Pager<>(page, pageSize, totalItems, list);
    }


    //获取一篇文章的详情。
    public Article wrapDetail(@NonNull Article article, String ip) {


        User user = userService.find(article.getUserUuid());
        article.setPosterTank(tankService.find(article.getPosterTankUuid()));
        article.setUser(user);

        //装点标签
        article.setTagArray(tagService.getTagsByUuids(JsonUtil.toStringList(article.getTags())));

        //统计文章点击数量。
        this.analysisHit(article, ip);

        //查看当前用户的点赞与否情况。
        History history = historyDao.findTopByTypeAndEntityUuidAndIp(History.Type.AGREE_ARTICLE, article.getUuid(), ip);
        if (history != null) {
            article.setAgreed(true);
        }

        //装载上访问路径
        article.loadVisitUrlAndPath(null, user);

        //对于文档，装饰其目录树。
        if (article.getType() == ArticleType.DOCUMENT) {
            //找出所有的子节点。
            Pager<Article> nodeArticlePager = this.page(
                    0,
                    Pager.MAX_PAGE_SIZE,
                    Sort.Direction.ASC,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    article.getUuid(),
                    null,
                    false
            );
            //找出文档下的所有节点
            List<Article> nodeArticles = nodeArticlePager.getData();
            nodeArticles.forEach(node -> {
                node.loadVisitUrlAndPath(article, user);
            });

            this.refineHierarchy(article, nodeArticles, user);

        } else if (article.getType() == ArticleType.DOCUMENT_PLACEHOLDER_ARTICLE || article.getType() == ArticleType.DOCUMENT_ARTICLE) {
            //对于文档中的文章，附加上其文档信息
            article.setDocument(this.find(article.getDocumentUuid()));
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

        String url = NetworkUtil.getHost() + "/home/article/" + article.getUuid();
        String html = "<p>文章:《" + article.getTitle() + "》</p><p>用户：" + comment.getName() + "</p><p>邮箱：" + comment.getEmail() + "</p><p>内容：" + comment.getContent() + "\"</p><p><a href=\"" + url + "\">点击查看</a></p>";
        NotificationResult notificationResult = mailService.htmlSend(user.getEmail(), "《" + article.getTitle() + "》收到新评论了", html);

        if (notificationResult.getStatus() != NotificationResult.Status.OK) {
            log.warn(html);
        }
    }


    //按照层级关系将菜单整理好。
    private void refineHierarchy(@NonNull Article document, @NonNull List<Article> candidates, @NonNull User user) {
        List<Article> nodes = new ArrayList<>();

        for (Article node : candidates) {
            //如果是顶级目录
            if (Objects.equals(node.getPuuid(), Article.ROOT)) {
                nodes.add(node);
                refineSubHierarchy(node, candidates);
            }
        }
        document.setChildren(nodes);
    }


    //递归整理当前菜单。
    private void refineSubHierarchy(@NonNull Article node, List<Article> candidates) {
        for (Article subNode : candidates) {
            if (node.getUuid().equals(subNode.getPuuid())) {
                node.addChild(subNode);
                refineSubHierarchy(subNode, candidates);
            }
        }
    }

    //对一篇文章的路径进行查重 前提是这篇文章还没有进行创建。或者修改了path.
    public void checkDuplicate(@NonNull User user, @NonNull Article article) {

        if (article.getType() == ArticleType.ARTICLE) {
            //查重。
            int count = articleDao.countByUserUuidAndTypeAndPath(user.getUuid(), ArticleType.ARTICLE, article.getPath());
            if (count > 0) {
                throw new BadRequestException("路径 {} 的文章已经存在，创建失败。", article.getPath());
            }

        } else if (article.getType() == ArticleType.DOCUMENT) {
            //查重。
            int count = articleDao.countByUserUuidAndTypeAndPath(user.getUuid(), ArticleType.DOCUMENT, article.getPath());
            if (count > 0) {
                throw new BadRequestException("路径 {} 的文档已经存在，创建失败。", article.getPath());
            }
        } else if (
                article.getType() == ArticleType.DOCUMENT_PLACEHOLDER_ARTICLE ||
                        article.getType() == ArticleType.DOCUMENT_ARTICLE
        ) {
            //查重。
            int count = articleDao.countByUserUuidAndDocumentUuidAndPath(user.getUuid(), article.getDocumentUuid(), article.getPath());
            if (count > 0) {
                throw new BadRequestException("路径 {} 的节点已经存在，创建失败。", article.getPath());
            }

        }

    }


    //递归删除目录.
    @Transactional
    public void documentIndexDel(@NonNull Article document, @NonNull Article article, boolean forceDelete) {

        //查看是否还有子集。
        List<Article> children = articleDao.findByDocumentUuidAndPuuid(document.getUuid(), article.getUuid());
        for (Article child : children) {
            documentIndexDel(document, child, forceDelete);
        }


        //删除当前的这篇。
        if (article.getType() == ArticleType.DOCUMENT_ARTICLE) {

            if (forceDelete) {
                //强制删除
                articleDao.delete(article);
            } else {
                //退回为普通文章
                article.setType(ArticleType.ARTICLE);
                article.setPuuid(Article.ROOT);
                article.setDocumentUuid(null);
                article.setPrivacy(true);

                articleDao.save(article);
            }

        } else {
            //新文章，空白，超链接直接删除即可。
            articleDao.delete(article);
        }

    }


}
