package cn.zicla.blog.rest.user;

import cn.zicla.blog.rest.base.BaseEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class UserService extends BaseEntityService<User> {

    @Autowired
    UserDao userDao;

    public UserService() {
        super(User.class);
    }

    //做权限拦截的事情。主要给AuthInterceptor使用。Hibernate Session必须通过@Transactional才可持久。
    @Transactional
    public boolean doAuthIntercept(HttpServletRequest request, HttpServletResponse response, Object handler) {

        return true;

    }

}
