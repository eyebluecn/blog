package cn.zicla.blog.rest.comment;

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

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/comment")
public class CommentController extends BaseEntityController<Comment, CommentForm> {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentDao commentDao;

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

        if (isFloor != null && isFloor && needSubPager != null && needSubPager) {
            pager.getData().forEach(comment -> {
                comment.setCommentPager(commentService.page(
                        0,
                        10,
                        Sort.Direction.DESC,
                        null,
                        articleUuid,
                        false,
                        comment.getUuid(),
                        comment.getUuid(),
                        null,
                        null,
                        null,
                        null,
                        null
                ));
            });
        }

        return this.success(pager);
    }

}
