package cn.eyeblue.blog.rest.report;

import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.report.Report;
import cn.eyeblue.blog.rest.report.ReportDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportService extends BaseEntityService<Report> {

    @Autowired
    ReportDao userDao;

    public ReportService() {
        super(Report.class);
    }

}
