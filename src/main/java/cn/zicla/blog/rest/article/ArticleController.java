package cn.zicla.blog.rest.article;

import cn.zicla.blog.rest.base.Base;
import cn.zicla.blog.rest.base.BaseEntityController;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.captcha.SupportCaptchaService;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import cn.zicla.blog.rest.tank.TankService;
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
@RequestMapping("/api/article")
public class ArticleController extends BaseEntityController<Article, ArticleForm> {

    @Autowired
    ArticleService articleService;

    @Autowired
    TankService tankService;

    @Autowired
    ArticleDao articleDao;

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

        Sort sort = new Sort(Sort.Direction.ASC, Article_.deleted.getName());

        if (orderSort != null) {
            sort = sort.and(new Sort(orderSort, Article_.sort.getName()));
        }

        if (orderTop != null) {
            sort = sort.and(new Sort(orderTop, Article_.top.getName()));
        }

        if (orderHit != null) {
            sort = sort.and(new Sort(orderHit, Article_.hit.getName()));
        }

        if (orderPrivacy != null) {
            sort = sort.and(new Sort(orderPrivacy, Article_.privacy.getName()));
        }

        if (orderReleaseTime != null) {
            sort = sort.and(new Sort(orderReleaseTime, Article_.releaseTime.getName()));
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);
        return this.success(((root, query, cb) -> {
            Predicate predicate = cb.equal(root.get(Article_.deleted), false);

            if (userUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Article_.userUuid), userUuid));
            }
            if (title != null) {
                predicate = cb.and(predicate, cb.like(root.get(Article_.title), "%" + title + "%"));
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

        }), pageable, Base::map);
    }

}
