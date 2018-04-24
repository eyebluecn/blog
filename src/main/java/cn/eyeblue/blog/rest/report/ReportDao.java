package cn.eyeblue.blog.rest.report;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
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
    @Query("DELETE Report r WHERE r.entityUuid =:entityUuid and r.uuid > '0'")
    int deleteByEntityUuid(@Param("entityUuid") String entityUuid);

    //将某个实体对应的report设置为已处理。
    int countByEntityUuid(String entityUuid);


}
