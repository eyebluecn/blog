package cn.eyeblue.blog.rest.user;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserDao extends BaseEntityDao<User> {


    User findByEmail(String email);

    int countByEmail(String email);

    User findTopByUsername(String username);

    //查询某个时间段内，用户的数量
    int countByCreateTimeBetween(Date startDate, Date endDate);

}
