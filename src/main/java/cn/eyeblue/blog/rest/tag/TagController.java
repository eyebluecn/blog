package cn.eyeblue.blog.rest.tag;

import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.BaseEntityController;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.common.MailService;
import cn.eyeblue.blog.rest.common.NotificationResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.support.captcha.SupportCaptchaService;
import cn.eyeblue.blog.rest.support.session.SupportSessionDao;
import cn.eyeblue.blog.rest.tank.TankService;
import cn.eyeblue.blog.rest.user.User;
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
@RequestMapping("/api/tag")
public class TagController extends BaseEntityController<Tag, TagForm> {

    @Autowired
    TagService tagService;

    @Autowired
    TankService tankService;

    @Autowired
    TagDao tagDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SupportCaptchaService supportCaptchaService;

    @Autowired
    MailService mailService;


    public TagController() {
        super(Tag.class);
    }


    @Override
    @Feature(FeatureType.USER_MINE)
    public WebResult create(@Valid TagForm form) {

        //检查name的重复性。
        User operator = checkUser();
        Tag tag = form.create(operator);




        //名字不能重复
        int count = tagDao.countByUserUuidAndNameAndDeletedFalse(operator.getUuid(), tag.getName());
        if (count > 0) {
            throw new UtilException("名称" + tag.getName() + "已经存在，请使用其他名称。");
        }

        tag = tagDao.save(tag);

        return success(tag);
    }

    @Override
    @Feature(FeatureType.USER_MINE)
    public WebResult del(@PathVariable String uuid) {
        Tag tag = this.check(uuid);

        //验证权限
        checkMineEntityPermission(FeatureType.USER_MANAGE, FeatureType.USER_MINE, tag.getUserUuid());

        tagDao.delete(tag);

        return success();
    }

    @Override
    @Feature(FeatureType.USER_MINE)
    public WebResult edit(@Valid TagForm form) {
        User operator = checkUser();
        Tag tag = this.check(form.getUuid());
        //验证权限
        checkMineEntityPermission(FeatureType.USER_MANAGE, FeatureType.USER_MINE, tag.getUserUuid());


        String oldName = tag.getName();
        form.update(tag, operator);
        String newName = tag.getName();

        if (!oldName.equals(newName)) {
            //邮箱变更了时就要检查唯一性。
            int count = tagDao.countByUserUuidAndNameAndDeletedFalse(operator.getUuid(), newName);
            if (count > 0) {
                throw new UtilException("名称" + tag.getName() + "已经存在，请使用其他名称。");
            }
        }

        tag = tagDao.save(tag);

        return success(tag);
    }

    @Override
    @Feature(FeatureType.PUBLIC)
    public WebResult detail(@PathVariable String uuid) {
        Tag tag = this.check(uuid);
        tag.setLogoTank(tankService.find(tag.getLogoTankUuid()));
        return success(tag);
    }

    @Override
    @Feature(FeatureType.USER_MINE)
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
            @RequestParam(required = false) String name
    ) {
        Pager<Tag> tagPager = tagService.page(
                page,
                pageSize,
                orderSort,
                userUuid,
                name);

        return this.success(tagPager);

    }

    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/test")
    public WebResult test() {

        //mailService.textSend("lish516@126.com", "你好啊，今天" + System.currentTimeMillis(), "现在时刻是 now date = " + System.currentTimeMillis());

        NotificationResult notificationResult = mailService.htmlSend("lish516@126.com", "你好啊，今天" + System.currentTimeMillis(), "<h1>您的文章" + System.currentTimeMillis() + "有新的评论</h1>");
        return success();
    }

}
