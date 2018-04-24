package cn.eyeblue.blog.rest.tag;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface TagDao extends BaseEntityDao<Tag> {

    int countByUserUuidAndName(String userUuid, String name);
}
