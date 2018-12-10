package cn.eyeblue.blog.rest.security.analysis;


import cn.eyeblue.blog.rest.base.BaseEntityController;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.tank.TankService;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/security/analysis")
public class SecurityAnalysisController extends BaseEntityController<SecurityAnalysis, SecurityAnalysisForm> {

    @Autowired
    SecurityAnalysisService securityAnalysisService;

    @Autowired
    TankService tankService;

    @Autowired
    SecurityAnalysisDao securityAnalysisDao;

    public SecurityAnalysisController() {
        super(SecurityAnalysis.class);
    }


    @Override
    @Feature(FeatureType.USER_MINE)
    public WebResult create(@Valid SecurityAnalysisForm form) {

        User operator = this.checkUser();
        SecurityAnalysis securityAnalysis = form.create(operator);

        securityAnalysisDao.save(securityAnalysis);

        return success(securityAnalysis);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult edit(@Valid SecurityAnalysisForm form) {

        SecurityAnalysis securityAnalysis = securityAnalysisService.edit(form, checkUser());

        return success(securityAnalysis);

    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult del(@PathVariable String uuid) {

        securityAnalysisService.del(uuid);
        return success();
    }

    @Override
    @Feature(FeatureType.PUBLIC)
    public WebResult detail(@PathVariable String uuid) {

        SecurityAnalysis securityAnalysis = this.check(uuid);


        return success(securityAnalysis);
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
            @RequestParam(required = false) @DateTimeFormat(pattern = DateUtil.DATE_FORMAT) Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = DateUtil.DATE_FORMAT) Date endTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = DateUtil.DATE_FORMAT) Date dt


    ) {

        Pager<SecurityAnalysis> pager = securityAnalysisService.page(
                page,
                pageSize,
                orderSort,
                orderUpdateTime,
                orderCreateTime,
                startTime == null ? null : new java.sql.Date(startTime.getTime()),
                endTime == null ? null : new java.sql.Date(endTime.getTime()),
                dt == null ? null : new java.sql.Date(dt.getTime())
        );

        return this.success(pager);
    }


    @Feature(FeatureType.USER_MANAGE)
    @RequestMapping("/etl")
    public WebResult etl(
            @RequestParam @DateTimeFormat(pattern = DateUtil.DEFAULT_FORMAT) Date day
    ) {
        SecurityAnalysis securityAnalysis = securityAnalysisService.doETL(day);
        return this.success(securityAnalysis);
    }

}
