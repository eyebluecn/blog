package cn.eyeblue.blog.rest.report;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import cn.eyeblue.blog.rest.report.Report;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDao extends BaseEntityDao<Report> {

    int countByTypeAndEntityUuidAndIp(Report.Type type, String entityUuid, String ip);

    Report findTopByTypeAndEntityUuidAndIp(Report.Type type, String entityUuid, String ip);

    //将某个实体对应的report设置为已处理。
    @Modifying
    @Query("UPDATE Report r SET r.deleted = true WHERE r.entityUuid =:entityUuid")
    int softDeleteByEntityUuid(@Param("entityUuid") String entityUuid);

    //将某个实体对应的report设置为已处理。
    int countByEntityUuidAndDeletedFalse(String entityUuid);


}
