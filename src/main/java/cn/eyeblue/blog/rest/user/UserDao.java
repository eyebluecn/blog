package cn.eyeblue.blog.rest.user;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseEntityDao<User> {


    User findByEmailAndDeletedFalse(String email);

    int countByEmail(String email);

}
