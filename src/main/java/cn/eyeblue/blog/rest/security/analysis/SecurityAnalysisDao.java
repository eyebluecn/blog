package cn.eyeblue.blog.rest.security.analysis;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface SecurityAnalysisDao extends BaseEntityDao<SecurityAnalysis> {

    //每天一条汇总信息。
    SecurityAnalysis findByDt(Date date);
}
