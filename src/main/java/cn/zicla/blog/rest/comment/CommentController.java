package cn.zicla.blog.rest.comment;

import cn.zicla.blog.config.exception.UtilException;
import cn.zicla.blog.rest.agree.History;
import cn.zicla.blog.rest.agree.HistoryDao;
import cn.zicla.blog.rest.agree.History_;
import cn.zicla.blog.rest.article.Article_;
import cn.zicla.blog.rest.base.BaseEntityController;
import cn.zicla.blog.rest.base.Pager;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.captcha.SupportCaptchaService;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import cn.zicla.blog.rest.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/comment")
public class CommentController extends BaseEntityController<Comment, CommentForm> {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentDao commentDao;

    @Autowired
    HistoryDao historyDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SupportCaptchaService supportCaptchaService;


    public CommentController() {
        super(Comment.class);
    }


    @Override
    @Feature(FeatureType.PUBLIC)
    public WebResult create(@Valid CommentForm form) {

        Comment comment = new Comment();
        User user = this.findUser();

        //如果有登录。
        if (this.findUser() != null) {
            comment.setUserUuid(user.getUuid());
        }

        form.update(comment, user);

        comment = commentDao.save(comment);
        return success(comment);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult del(@PathVariable String uuid) {
        Comment entity = this.check(uuid);
        commentDao.delete(entity);

        return success();
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult edit(@Valid CommentForm form) {
        return super.edit(form);
    }

    @Override
    @Feature(FeatureType.PUBLIC)
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
            @RequestParam(required = false) String userUuid,
            @RequestParam(required = false) String articleUuid,
            @RequestParam(required = false) Boolean isFloor,
            @RequestParam(required = false) String floorUuid,
            @RequestParam(required = false) String puuid,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Boolean isReport,
            @RequestParam(required = false) String report,
            //在isFloor=true的情况下，是否要求展示子Pager的东西。
            @RequestParam(required = false) Boolean needSubPager
    ) {

        Pager<Comment> pager = commentService.page(
                page,
                pageSize,
                orderSort,
                userUuid,
                articleUuid,
                isFloor,
                floorUuid,
                puuid,
                name,
                email,
                content,
                isReport,
                report
        );

        List<String> commentUuids = new ArrayList<>();

        pager.getData().forEach(comment1 -> {
            commentUuids.add(comment1.getUuid());
        });


        if (isFloor != null && isFloor && needSubPager != null && needSubPager) {
            pager.getData().forEach(comment -> {

                Pager<Comment> subPager = commentService.page(
                        0,
                        10,
                        Sort.Direction.DESC,
                        null,
                        articleUuid,
                        false,
                        comment.getUuid(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );

                subPager.getData().forEach(comment1 -> {
                    commentUuids.add(comment1.getUuid());
                });


                comment.setCommentPager(subPager);
            });
        }


        if (commentUuids.size() > 0) {
            //一口气查询所有的comment点赞情况。
            String ip = getCurrentRequestIp();
            System.out.println(ip);
            List<History> histories = historyDao.findAll((root, query, cb) -> {
                Predicate predicate = cb.equal(root.get(History_.deleted), false);

                predicate = cb.and(predicate, cb.equal(root.get(History_.type), History.Type.AGREE_COMMENT));

                predicate = cb.and(predicate, cb.equal(root.get(History_.ip), ip));

                Predicate orPredicates = null;
                for (String commentUuid : commentUuids) {
                    if (orPredicates == null) {
                        orPredicates = cb.equal(root.get(History_.entityUuid), commentUuid);

                    } else {
                        orPredicates = cb.or(orPredicates, cb.equal(root.get(History_.entityUuid), commentUuid));
                    }
                }
                predicate = cb.and(predicate, orPredicates);

                return predicate;

            });

            Set<String> uuidSet = histories.stream().map(History::getEntityUuid).collect(Collectors.toSet());

            //依次检查每个comment的情况。
            pager.getData().forEach(comment -> {
                if (uuidSet.contains(comment.getUuid())) {
                    comment.setAgreed(true);
                }

                if (comment.getCommentPager() != null) {
                    comment.getCommentPager().getData().forEach(comment1 -> {
                        if (uuidSet.contains(comment1.getUuid())) {
                            comment1.setAgreed(true);
                        }
                    });
                }
            });
        }


        return this.success(pager);
    }

    //给某条评论点赞。
    @RequestMapping("/agree")
    @Feature(FeatureType.PUBLIC)
    public WebResult agree(@RequestParam String commentUuid) {

        Comment comment = this.check(commentUuid);


        String ip = getCurrentRequestIp();
        System.out.println(ip);
        int count = historyDao.countByTypeAndEntityUuidAndIp(History.Type.AGREE_COMMENT, commentUuid, ip);
        if (count > 0) {
            throw new UtilException("请勿重复点赞！");
        }

        History history = new History();
        history.setType(History.Type.AGREE_COMMENT);
        history.setEntityUuid(commentUuid);
        history.setIp(ip);
        historyDao.save(history);

        comment.setAgree(comment.getAgree() + 1);
        commentDao.save(comment);

        return success("点赞成功!");
    }


    //取消点赞。
    @RequestMapping("/cancel/agree")
    @Feature(FeatureType.PUBLIC)
    public WebResult cancelAgree(@RequestParam String commentUuid) {

        Comment comment = this.check(commentUuid);

        String ip = getCurrentRequestIp();
        History history = historyDao.findTopByTypeAndEntityUuidAndIp(History.Type.AGREE_COMMENT, commentUuid, ip);
        if (history == null) {
            throw new UtilException("您没有点赞过这条评论，操作失败！");
        }

        historyDao.delete(history);
        comment.setAgree(comment.getAgree() - 1);
        commentDao.save(comment);

        return success("取消点赞成功!");
    }


}
