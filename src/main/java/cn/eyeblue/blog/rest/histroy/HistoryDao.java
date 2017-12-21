package cn.eyeblue.blog.rest.histroy;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryDao extends BaseEntityDao<History> {

    int countByTypeAndEntityUuidAndIp(History.Type type, String entityUuid, String ip);

    History findTopByTypeAndEntityUuidAndIp(History.Type type, String entityUuid, String ip);

}
