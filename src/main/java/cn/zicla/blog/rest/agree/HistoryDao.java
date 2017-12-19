package cn.zicla.blog.rest.agree;

import cn.zicla.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryDao extends BaseEntityDao<History> {

    int countByTypeAndEntityUuidAndIp(History.Type type, String entityUuid, String ip);


}
