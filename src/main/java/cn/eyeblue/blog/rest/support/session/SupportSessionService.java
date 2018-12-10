package cn.eyeblue.blog.rest.support.session;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.common.AsyncHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SupportSessionService extends BaseEntityService<SupportSession> {

    @Autowired
    SupportSessionDao supportSessionDao;

    @Autowired
    AsyncHandlerService asyncHandlerService;

    public SupportSessionService() {
        super(SupportSession.class);
    }

    //每小时自动执行一次
    @Scheduled(cron = "0 0 * * * ?")
    public void startScheduleTask() {

        asyncHandlerService.submit(() -> {
            log.info("---定时清理过期session---");
            this.cleanExpired();
            log.info("---完成清理过期session---");
        });
    }

    //清除那些过期的session.
    private void cleanExpired() {

        //把这条提醒送到相应的邮箱中去。
        Specification<SupportSession> specification =
                (root, query, cb) -> cb.lessThanOrEqualTo(root.get(SupportSession_.expireTime), new Date());

        List<SupportSession> supportSessionList = new ArrayList<>();
        //每次处理500条订阅信息。
        AppContextManager.pageHandle(SupportSession.class, specification, Pager.HANDLE_PAGE_SIZE, supportSessionList::add);

        supportSessionDao.deleteAll(supportSessionList);

    }


}
