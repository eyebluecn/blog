package cn.zicla.blog.rest.user;

import cn.zicla.blog.config.exception.LoginException;
import cn.zicla.blog.config.exception.NotFoundException;
import cn.zicla.blog.config.exception.UnauthorizedException;
import cn.zicla.blog.config.exception.UtilException;
import cn.zicla.blog.rest.base.BaseEntityService;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.session.SupportSession;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@Service
public class UserService extends BaseEntityService<User> {

    @Autowired
    UserDao userDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    public UserService() {
        super(User.class);
    }

    //做权限拦截的事情。主要给AuthInterceptor使用。Hibernate Session必须通过@Transactional才可持久。
    @Transactional
    public boolean doAuthIntercept(HttpServletRequest request, HttpServletResponse response, Object handler) {


        HandlerMethod handlerMethod = null;
        try {
            handlerMethod = (HandlerMethod) handler;
        } catch (Exception e) {
            throw new UtilException("转换HandlerMethod出错，请及时排查。");
        }

        //系统自带的接口
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/error")) {
            return true;
        }


        //验证用户的身份，是否已经登录了。
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute(User.TAG);

        if (user == null) {

            //尝试去验证token。
            Cookie[] cookies = request.getCookies();
            String authentication = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {

                    if (WebResult.COOKIE_AUTHENTICATION.equals(cookie.getName())) {
                        authentication = cookie.getValue();
                    }
                }
            }

            if (authentication != null) {


                request.getSession().setAttribute(WebResult.COOKIE_AUTHENTICATION, authentication);

                SupportSession supportSession = supportSessionDao.findOne(authentication);

                if (supportSession != null) {

                    if (supportSession.getExpireTime().getTime() > System.currentTimeMillis()) {

                        String userUuid = supportSession.getUserUuid();
                        user = userDao.findOne(userUuid);

                        if (user != null) {

                            httpSession.setAttribute(User.TAG, user);

                            //更新该用户的登录信息
                            supportSession.setExpireTime(new Date(System.currentTimeMillis() + SupportSession.EXPIRY * 1000));
                            supportSessionDao.save(supportSession);
                        }

                    }
                }


            }
        }

        Feature feature = handlerMethod.getMethodAnnotation(Feature.class);

        if (feature != null) {
            FeatureType[] types = feature.value();

            //公共接口直接允许访问。
            if (types[0] == FeatureType.PUBLIC) {

                return true;

            } else {

                if (user == null) {
                    throw new LoginException();
                }

                //2.验证用户是否有权限访问该接口。
                StringBuilder typeString = new StringBuilder();
                for (FeatureType featureType : types) {

                    typeString.append(featureType).append(",");
                    if (user.hasPermission(featureType)) {
                        return true;
                    }

                }

                log.error("用户：" + user.getUsername() + " 角色：" + user.getRole() + " 功能点：" + typeString.toString() + " 接口：" + request.getRequestURI());
                throw new UnauthorizedException();

            }

        } else {
            throw new NotFoundException("您请求的内容不存在！");
        }


    }

}
