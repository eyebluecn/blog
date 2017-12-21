package cn.eyeblue.blog.rest.report;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import cn.eyeblue.blog.rest.report.Report;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDao extends BaseEntityDao<Report> {

    int countByTypeAndEntityUuidAndIp(Report.Type type, String entityUuid, String ip);

    Report findTopByTypeAndEntityUuidAndIp(Report.Type type, String entityUuid, String ip);

}
