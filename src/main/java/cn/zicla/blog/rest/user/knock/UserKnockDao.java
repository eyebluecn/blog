package cn.zicla.blog.rest.user.knock;

import cn.zicla.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserKnockDao extends BaseEntityDao<UserKnock> {

    long countByIpAndTypeNotAndCreateTimeAfter(String ip, UserKnock.Type type, Date then);
}
