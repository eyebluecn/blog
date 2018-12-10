package cn.eyeblue.blog.rest.security.visit;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SecurityVisitDao extends BaseEntityDao<SecurityVisit> {


    //查询某个时间段内，记录的数量
    int countByCreateTimeBetween(Date startDate, Date endDate);


    //查询某个时间段内的uv
    @Query("SELECT COUNT(DISTINCT v.userUuid) FROM SecurityVisit v WHERE v.createTime BETWEEN :startTime and :endTime")
    int findUV(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );

}
