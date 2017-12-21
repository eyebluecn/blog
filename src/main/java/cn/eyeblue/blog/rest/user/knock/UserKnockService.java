package cn.eyeblue.blog.rest.user.knock;

import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.rest.user.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class UserKnockService extends BaseEntityService<User> {

    @Autowired
    UserDao userDao;

    @Autowired
    UserKnockDao userKnockDao;

    public UserKnockService() {
        super(User.class);
    }


    //某个ip是否需要验证码。
    //判断依据，最近三天是否有登录失败过。
    public boolean needCaptcha(String ip) {

        //三天之内。
        Date date = new Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000);

        long times = userKnockDao.countByIpAndTypeNotAndCreateTimeAfter(ip, UserKnock.Type.OK, date);

        return times > 0;

    }

    //记录用户登录情况
    public void log(String sessionId, String userUuid, String username, String password, String ip, UserKnock.Type type) {

        UserKnock userKnock = new UserKnock(sessionId, userUuid, username, password, ip, type);

        userKnockDao.save(userKnock);




    }

}
