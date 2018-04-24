package cn.eyeblue.blog.rest.user;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseEntityDao<User> {


    User findByEmail(String email);

    int countByEmail(String email);

    User findTopByUsername(String username);
}
