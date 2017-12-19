package cn.zicla.blog.rest.agree;

import cn.zicla.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryDao extends BaseEntityDao<History> {

    int countByTypeAndArticleUuidAndIp(History.Type type, String articleUuid, String ip);

    int countByTypeAndCommentUuidAndIp(History.Type type, String commentUuid, String ip);

}
