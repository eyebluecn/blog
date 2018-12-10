package cn.eyeblue.blog.rest.security.visit;


import cn.eyeblue.blog.rest.base.BaseEntityController;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.tank.TankService;
import cn.eyeblue.blog.rest.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/security/visit")
public class SecurityVisitController extends BaseEntityController<SecurityVisit, SecurityVisitForm> {

    @Autowired
    SecurityVisitService securityVisitService;

    @Autowired
    TankService tankService;

    @Autowired
    SecurityVisitDao securityVisitDao;


    public SecurityVisitController() {
        super(SecurityVisit.class);
    }


    @Override
    @Feature(FeatureType.USER_MINE)
    public WebResult create(@Valid SecurityVisitForm form) {

        User operator = this.checkUser();
        SecurityVisit securityVisit = form.create(operator);

        securityVisitDao.save(securityVisit);

        return success(securityVisit);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult edit(@Valid SecurityVisitForm form) {

        SecurityVisit securityVisit = securityVisitService.edit(form, checkUser());

        return success(securityVisit);

    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult del(@PathVariable String uuid) {

        securityVisitService.del(uuid);
        return success();
    }

    @Override
    @Feature(FeatureType.PUBLIC)
    public WebResult detail(@PathVariable String uuid) {

        SecurityVisit securityVisit = this.check(uuid);


        return success(securityVisit);
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
            @RequestParam(required = false) Sort.Direction orderUpdateTime,
            @RequestParam(required = false) Sort.Direction orderCreateTime,
            @RequestParam(required = false) Date startTime,
            @RequestParam(required = false) Date endTime,
            @RequestParam(required = false) String userUuid

    ) {

        Pager<SecurityVisit> pager = securityVisitService.page(
                page,
                pageSize,
                orderSort,
                orderUpdateTime,
                orderCreateTime,
                startTime,
                endTime,
                userUuid
        );

        return this.success(pager);
    }

}
