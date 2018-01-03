package cn.eyeblue.blog.rest.report;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import cn.eyeblue.blog.rest.report.Report;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDao extends BaseEntityDao<Report> {

    int countByTypeAndEntityUuidAndIp(Report.Type type, String entityUuid, String ip);

    Report findTopByTypeAndEntityUuidAndIp(Report.Type type, String entityUuid, String ip);


    //将某个实体对应的report设置为已处理。
    @Modifying
    @Query("UPDATE Report r SET r.handled = true WHERE r.entityUuid =?1 AND r.handled = false")
    int markHandled(String entityUuid);

    //将某个实体对应的report设置为已处理。
    int countByEntityUuidAndHandledFalseAndDeletedFalse(String entityUuid);


}
