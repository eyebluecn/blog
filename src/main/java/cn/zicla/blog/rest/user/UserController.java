package cn.zicla.blog.rest.user;

import cn.zicla.blog.config.exception.BadRequestException;
import cn.zicla.blog.config.exception.UtilException;
import cn.zicla.blog.rest.base.BaseEntityController;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.captcha.SupportCaptchaService;
import cn.zicla.blog.rest.support.session.SupportSession;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import cn.zicla.blog.rest.user.knock.UserKnock;
import cn.zicla.blog.rest.user.knock.UserKnockService;
import cn.zicla.blog.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseEntityController<User, UserForm> {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    @Autowired
    UserKnockService userKnockService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SupportCaptchaService supportCaptchaService;


    public UserController() {
        super(User.class);
    }


    @Feature(FeatureType.USER_MANAGE)
    @RequestMapping("/page")
    public WebResult page(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Sort.Direction orderLastTime,
            @RequestParam(required = false) Sort.Direction orderSort,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "false") Boolean autoComplete
    ) {

        Sort sort = new Sort(Sort.Direction.ASC, User_.deleted.getName());

        if (orderSort != null) {
            sort = sort.and(new Sort(orderSort, User_.sort.getName()));
        }
        if (orderLastTime != null) {
            sort = sort.and(new Sort(orderLastTime, User_.lastTime.getName()));
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);
        return this.success(((root, query, cb) -> {
                    Predicate predicate = cb.equal(root.get(User_.deleted), false);

                    if (username != null) {
                        predicate = cb.and(predicate, cb.like(root.get(User_.username), "%" + username + "%"));
                    }
                    if (keyword != null) {

                        Predicate predicate1 = cb.like(root.get(User_.username), "%" + keyword + "%");
                        Predicate predicate2 = cb.like(root.get(User_.email), "%" + keyword + "%");

                        predicate = cb.and(predicate, cb.or(predicate1, predicate2));
                    }
                    return predicate;

                })
                , pageable,
                user -> {
                    if (autoComplete) {
                        return user.simpleMap();
                    } else {
                        return user.detailMap();
                    }
                });


    }

    //登录验证通过或者注册成功后内部自动登录。
    private WebResult innerLogin(
            User user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String ip = NetworkUtil.getIpAddress(request);

        log.info(user.getUsername() + " login from ip:" + ip);

        //Store user into session.
        request.getSession().setAttribute(user.getTAG(), user);


        SupportSession supportSession = new SupportSession(user.getUuid(), ip, new Date(System.currentTimeMillis() + SupportSession.EXPIRY * 1000));
        supportSessionDao.save(supportSession);

        //设置浏览器cookie.
        Cookie cookie = new Cookie(WebResult.COOKIE_AUTHENTICATION, supportSession.getUuid());
        cookie.setMaxAge(SupportSession.EXPIRY);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return this.success(user);
    }


    /**
     * 用户登录
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping(value = "/login")
    public WebResult login(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam(required = false) String captcha,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        User user = userDao.findByEmailAndDeletedFalse(email);

        String sessionId = request.getSession().getId();
        String ip = NetworkUtil.getIpAddress(request);

        if (user != null) {

            //登录错误次数超过1次必须要输入验证码。
            if (userKnockService.needCaptcha(ip)) {
                if (!supportCaptchaService.validate(sessionId, captcha)) {

                    userKnockService.log(sessionId, user.getUuid(), email, password, ip, UserKnock.Type.CAPTCHA_ERROR);
                    throw new UtilException("验证码错误！");
                }
            }

            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {

                //更新user上次登录ip和时间
                user.setLastIp(ip);
                user.setLastTime(new Date());
                userDao.save(user);

                return this.innerLogin(user, request, response);
            } else {
                userKnockService.log(sessionId, null, email, password, ip, UserKnock.Type.PASSWORD_ERROR);
            }
        } else {
            userKnockService.log(sessionId, null, email, password, ip, UserKnock.Type.NO_SUCH_USERNAME);
        }

        throw new BadRequestException("用户名或密码错误");

    }

    /**
     * 退出登录
     */
    @Feature(FeatureType.USER_MINE)
    @RequestMapping(value = "/logout")
    public WebResult logout(HttpServletRequest request,
                            HttpServletResponse response) {

        User user = checkUser();
        HttpSession httpSession = request.getSession();

        //尝试去获取cookie中的值
        Cookie[] cookies = request.getCookies();
        String authentication = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (WebResult.COOKIE_AUTHENTICATION.equals(cookie.getName())) {
                    authentication = cookie.getValue();
                }
            }
        }

        //清除cookie
        Cookie cookie = new Cookie(WebResult.COOKIE_AUTHENTICATION, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        //清除session.
        httpSession.removeAttribute(user.getTAG());
        if (authentication != null) {

            SupportSession supportSession = supportSessionDao.findOne(authentication);
            if (supportSession != null) {
                supportSessionDao.delete(supportSession);
            }
        } else {
            throw new BadRequestException("已经退出，或者登录凭证已失效。");
        }

        return success();
    }


}
