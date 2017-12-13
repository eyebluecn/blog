package cn.zicla.blog.rest.comment;

import cn.zicla.blog.rest.base.Base;
import cn.zicla.blog.rest.base.BaseEntityController;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.captcha.SupportCaptchaService;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import cn.zicla.blog.rest.comment.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
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
    @Feature(FeatureType.USER_MANAGE)
    public WebResult create(@Valid CommentForm form) {
        return super.create(form);
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


    @Feature(FeatureType.USER_MANAGE)
    @RequestMapping("/page")
    public WebResult page(

            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Sort.Direction orderSort,
            @RequestParam(required = false) String userUuid,
            @RequestParam(required = false) Boolean isFloor,
            @RequestParam(required = false) String floorUuid,
            @RequestParam(required = false) String puuid,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Boolean isReport,
            @RequestParam(required = false) String report
    ) {

        Sort sort = new Sort(Sort.Direction.ASC, Comment_.deleted.getName());

        if (orderSort != null) {
            sort = sort.and(new Sort(orderSort, Comment_.sort.getName()));
        }


        Pageable pageable = getPageRequest(page, pageSize, sort);
        return this.success(((root, query, cb) -> {
            Predicate predicate = cb.equal(root.get(Comment_.deleted), false);

            if (userUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.userUuid), userUuid));
            }
            if (isFloor != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.isFloor), isFloor));
            }
            if (floorUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.floorUuid), floorUuid));
            }
            if (puuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.puuid), puuid));
            }

            if (name != null) {
                predicate = cb.and(predicate, cb.like(root.get(Comment_.name), "%" + name + "%"));
            }

            if (email != null) {
                predicate = cb.and(predicate, cb.like(root.get(Comment_.email), "%" + email + "%"));
            }
            if (content != null) {
                predicate = cb.and(predicate, cb.like(root.get(Comment_.content), "%" + content + "%"));
            }
            if (isReport != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.isReport), isReport));
            }
            if (report != null) {
                predicate = cb.and(predicate, cb.like(root.get(Comment_.report), "%" + report + "%"));
            }
            return predicate;

        }), pageable, Base::map);
    }

}
