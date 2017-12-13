package cn.zicla.blog.rest.tag;

import cn.zicla.blog.rest.base.Base;
import cn.zicla.blog.rest.base.BaseEntityController;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.captcha.SupportCaptchaService;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
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
@RequestMapping("/api/tag")
public class TagController extends BaseEntityController<Tag, TagForm> {

    @Autowired
    TagService tagService;

    @Autowired
    TagDao tagDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SupportCaptchaService supportCaptchaService;


    public TagController() {
        super(Tag.class);
    }


    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult create(@Valid TagForm form) {
        return super.create(form);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult del(@PathVariable String uuid) {
        Tag entity = this.check(uuid);
        tagDao.delete(entity);

        return success();
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult edit(@Valid TagForm form) {
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
            @RequestParam(required = false) String name
    ) {

        Sort sort = new Sort(Sort.Direction.ASC, Tag_.deleted.getName());

        if (orderSort != null) {
            sort = sort.and(new Sort(orderSort, Tag_.sort.getName()));
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);
        return this.success(((root, query, cb) -> {
            Predicate predicate = cb.equal(root.get(Tag_.deleted), false);

            if (userUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Tag_.userUuid), userUuid));
            }
            if (name != null) {
                predicate = cb.and(predicate, cb.like(root.get(Tag_.name), "%" + name + "%"));
            }

            return predicate;

        }), pageable, Base::map);
    }

}
