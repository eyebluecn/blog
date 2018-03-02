package cn.eyeblue.blog.rest.user;

import cn.eyeblue.blog.config.exception.LoginException;
import cn.eyeblue.blog.config.exception.NotFoundException;
import cn.eyeblue.blog.config.exception.UnauthorizedException;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.article.ArticleDao;
import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.support.session.SupportSession;
import cn.eyeblue.blog.rest.support.session.SupportSessionDao;
import cn.eyeblue.blog.rest.tank.TankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserService extends BaseEntityService<User> {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    UserDao userDao;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    TankService tankService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    SupportSessionDao supportSessionDao;

    public UserService() {
        super(User.class);
    }

    //初始化一个超级管理员。
    public void initAdmin() {
        long count = userDao.count();
        if (count > 0) {
            return;
        }
        User user = new User();
        user.setUsername(adminUsername);
        user.setEmail(adminEmail);
        user.setPassword(bCryptPasswordEncoder.encode(adminPassword));
        user.setRole(User.Role.ADMIN);

        userDao.save(user);

    }

    //获取用户详情
    public User detail(String uuid) {
        //准备用户详情。
        User user = this.check(uuid);

        Object obj = articleDao.analysisTotal(user.getUuid());

        user.setArticleNum(articleDao.countByUserUuidAndPrivacyFalseAndDeletedFalse(uuid));

        if (obj != null) {
            Object[] list = (Object[]) obj;
            if (list.length > 0 && list[0] != null) {
                user.setArticleAgreeNum((Long) list[0]);
            }
            if (list.length > 1 && list[1] != null) {
                user.setArticleWords((Long) list[1]);
            }

            if (list.length > 2 && list[2] != null) {
                user.setArticleHit((Long) list[2]);
            }

            if (list.length > 3 && list[3] != null) {
                user.setCommentNum((Long) list[3]);
            }

        }


        user.setAvatar(tankService.find(user.getAvatarTankUuid()));

        return user;
    }

    //做权限拦截的事情。主要给AuthInterceptor使用。Hibernate Session必须通过@Transactional才可持久。
    @Transactional
    public boolean doAuthIntercept(HttpServletRequest request, HttpServletResponse response, Object handler) {


        HandlerMethod handlerMethod = null;
        if (handler instanceof ResourceHttpRequestHandler) {
            //资源文件处理器的拦截
            return true;
        } else {
            try {
                handlerMethod = (HandlerMethod) handler;
            } catch (Exception e) {
                throw new UtilException("转换HandlerMethod出错，请及时排查。");
            }
        }


        //系统自带的接口
        String requestURI = request.getRequestURI();
        if ("/error".equals(requestURI)) {
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

                Optional<SupportSession> optionalSupportSession = supportSessionDao.findById(authentication);
                //SupportSession supportSession = supportSessionDao.findOne(authentication);

                if (optionalSupportSession.isPresent()) {
                    SupportSession supportSession = optionalSupportSession.get();
                    if (supportSession.getExpireTime().getTime() > System.currentTimeMillis()) {

                        String userUuid = supportSession.getUserUuid();
                        Optional<User> optionalUser = userDao.findById(userUuid);
                        //user = userDao.findOne(userUuid);

                        if (optionalUser.isPresent()) {
                            user = optionalUser.get();

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
