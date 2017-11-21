package cn.zicla.blog.rest.user;

import cn.zicla.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseEntityDao<User> {


    User findByEmailAndDeletedFalse(String email);
}
