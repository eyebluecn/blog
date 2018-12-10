package cn.eyeblue.blog.rest.security.analysis;

import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.common.AsyncHandlerService;
import cn.eyeblue.blog.rest.security.visit.SecurityVisitDao;
import cn.eyeblue.blog.rest.user.UserDao;
import cn.eyeblue.blog.util.DateUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SecurityAnalysisService extends BaseEntityService<SecurityAnalysis> {

    @Autowired
    SecurityAnalysisDao securityAnalysisDao;

    @Autowired
    SecurityVisitDao securityVisitDao;


    @Autowired
    AsyncHandlerService asyncHandlerService;


    @Autowired
    UserDao userDao;


    public SecurityAnalysisService() {
        super(SecurityAnalysis.class);
    }


    public Pager<SecurityAnalysis> page(
            Integer page,
            Integer pageSize,
            Sort.Direction orderSort,
            Sort.Direction orderUpdateTime,
            Sort.Direction orderCreateTime,
            java.sql.Date startTime,
            java.sql.Date endTime,
            java.sql.Date dt
    ) {

        Sort sort = defaultSort(orderSort, orderUpdateTime, orderCreateTime);

        Pageable pageable = getPageRequest(page, pageSize, sort);

        Page<SecurityAnalysis> pageData = getDao().findAll((root, query, cb) -> {

            Predicate predicate = cb.isNotNull(root.get(SecurityAnalysis_.uuid));

            if (dt != null) {
                predicate = cb.and(predicate, cb.equal(root.get(SecurityAnalysis_.dt), dt));
            }

            if (startTime != null && endTime != null) {
                predicate = cb.and(predicate, cb.between(root.get(SecurityAnalysis_.dt), startTime, endTime));
            }

            return predicate;

        }, pageable);

        long totalItems = pageData.getTotalElements();
        List<SecurityAnalysis> list = pageData.getContent();

        return new Pager<>(pageable.getPageNumber(), pageable.getPageSize(), totalItems, list);
    }

    @Transactional
    public void del(String uuid) {
        SecurityAnalysis securityAnalysis = this.check(uuid);

        securityAnalysisDao.delete(securityAnalysis);

    }


    //每天2点30分自动添加。
    @Scheduled(cron = "0 30 2 * * ?")
    public void startScheduleTask() {

        asyncHandlerService.submit(() -> {
            log.info("---每日02:30自动清洗统计数据---");
            this.doETL(DateUtil.getYesterday());
        });
    }


    //执行数据清洗的操作。
    public SecurityAnalysis doETL(@NonNull Date thenDate) {

        Date yesterdayStartTime = DateUtil.getStartDate(thenDate);
        Date yesterdayEndTime = DateUtil.getEndDate(thenDate);

        log.info("两个时间：{} {}", yesterdayStartTime, yesterdayEndTime);

        java.sql.Date dt = new java.sql.Date(yesterdayStartTime.getTime());
        long pv = securityVisitDao.countByCreateTimeBetween(yesterdayStartTime, yesterdayEndTime);
        long uv = securityVisitDao.findUV(yesterdayStartTime, yesterdayEndTime);
        long userNum = userDao.count();
        long newUserNum = userDao.countByCreateTimeBetween(yesterdayStartTime, yesterdayEndTime);
        long amount = 0;


        SecurityAnalysis securityAnalysis = securityAnalysisDao.findByDt(dt);
        if (securityAnalysis == null) {
            securityAnalysis = new SecurityAnalysis(
                    dt,
                    pv,
                    uv,
                    userNum,
                    newUserNum,
                    amount
            );
        } else {
            securityAnalysis.update(
                    dt,
                    pv,
                    uv,
                    userNum,
                    newUserNum,
                    amount
            );
        }
        securityAnalysisDao.save(securityAnalysis);


        log.info("清洗数据完成 {}", dt);

        return securityAnalysis;
    }

}
