package cn.eyeblue.blog.rest.user;

import cn.eyeblue.blog.config.exception.BadRequestException;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.BaseEntityController;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.support.captcha.SupportCaptchaService;
import cn.eyeblue.blog.rest.support.session.SupportSession;
import cn.eyeblue.blog.rest.support.session.SupportSessionDao;
import cn.eyeblue.blog.rest.tank.TankService;
import cn.eyeblue.blog.rest.user.knock.UserKnock;
import cn.eyeblue.blog.rest.user.knock.UserKnockService;
import cn.eyeblue.blog.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseEntityController<User, UserForm> {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    TankService tankService;

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

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult create(@Valid UserForm form) {

        //检查email的重复性。
        User operator = checkUser();
        User user = form.create(operator);

        //邮箱不能重复
        int count = userDao.countByEmail(user.getEmail());
        if (count > 0) {
            throw new UtilException("邮箱" + user.getEmail() + "已经存在，请使用其他邮箱。");
        }

        user = userDao.save(user);

        return success(user);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult del(@PathVariable String uuid) {
        return super.del(uuid);
    }

    @Override
    @Feature(FeatureType.USER_MINE)
    public WebResult edit(@Valid UserForm form) {

        //检查email的重复性。
        User operator = checkUser();

        User user = this.check(form.getUuid());

        //验证权限
        checkMineEntityPermission(FeatureType.USER_MANAGE, FeatureType.USER_MINE, user.getUuid());


        String oldEmail = user.getEmail();
        form.update(user, operator);
        String newEmail = user.getEmail();

        if (!oldEmail.equals(newEmail)) {
            //邮箱变更了时就要检查唯一性。
            int count = userDao.countByEmail(newEmail);
            if (count > 0) {
                throw new UtilException("邮箱" + newEmail + "已经存在，请使用其他邮箱。");
            }
        }

        user = userDao.save(user);

        return success(user);


    }

    @Override
    @Feature(FeatureType.PUBLIC)
    public WebResult detail(@PathVariable String uuid) {
        User user = this.check(uuid);
        user.setAvatar(tankService.find(uuid));
        return success(user);
    }

    @Override
    @Feature(FeatureType.USER_MANAGE)
    public WebResult sort(@RequestParam String uuid1, @RequestParam Long sort1, @RequestParam String uuid2, @RequestParam Long sort2) {
        return super.sort(uuid1, sort1, uuid2, sort2);
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
        request.getSession().setAttribute(User.TAG, user);


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
    public WebResult login(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String captcha,
            HttpServletRequest request,
            HttpServletResponse response) {


        String sessionId = request.getSession().getId();
        String ip = NetworkUtil.getIpAddress(request);
        //登录错误次数超过1次必须要输入验证码。
        if (userKnockService.needCaptcha(ip)) {
            if (!supportCaptchaService.validate(sessionId, captcha)) {
                userKnockService.log(sessionId, null, email, password, ip, UserKnock.Type.CAPTCHA_ERROR);
                throw new UtilException("验证码错误！");
            }
        }

        User user = userDao.findByEmailAndDeletedFalse(email);


        if (user != null) {

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
        httpSession.removeAttribute(User.TAG);
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


    /**
     * 用户自己修改密码
     */
    @Feature(FeatureType.USER_MINE)
    @RequestMapping(value = "/change/password")
    public WebResult changePassword(String oldPassword, String newPassword) {

        User user = checkUser();

        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UtilException("旧密码错误！");
        }

        if (newPassword.length() < 6) {
            throw new UtilException("密码位数不能低于6位");
        }

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userDao.save(user);

        return success();
    }


    /**
     * 管理员重置某个用户的密码
     */
    @Feature(FeatureType.USER_MANAGE)
    @RequestMapping(value = "/reset/password")
    public WebResult resetPassword(String userUuid, String newPassword) {

        User user = this.check(userUuid);

        if (newPassword.length() < 6) {
            throw new UtilException("密码位数不能低于6位");
        }

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userDao.save(user);

        return success();

    }


}
