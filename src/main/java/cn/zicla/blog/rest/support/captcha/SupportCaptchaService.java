package cn.zicla.blog.rest.support.captcha;

import cn.zicla.blog.rest.base.BaseEntityService;
import cn.zicla.blog.rest.user.User;
import cn.zicla.blog.rest.user.UserDao;
import cn.zicla.blog.rest.user.knock.UserKnock;
import cn.zicla.blog.rest.user.knock.UserKnockDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class SupportCaptchaService extends BaseEntityService<SupportCaptcha> {

    @Autowired
    SupportCaptchaDao supportCaptchaDao;

    public SupportCaptchaService() {
        super(SupportCaptcha.class);
    }

    public boolean validate(String sessionId, String captchaValue) {

        SupportCaptcha supportCaptcha = supportCaptchaDao.findBySessionId(sessionId);

        if (supportCaptcha != null) {

            //1.验证正确性。2.验证是否超时
            if (supportCaptcha.getValue().equalsIgnoreCase(captchaValue)) {

                log.debug("验证码: " + supportCaptcha.getValue() + " 正确性通过！");

                if (supportCaptcha.getExpireTime().getTime() > System.currentTimeMillis()) {

                    log.debug("验证码: " + supportCaptcha.getValue() + " 时效性通过！");

                    supportCaptchaDao.delete(supportCaptcha);

                    return true;
                }

            }
        }
        return false;
    }


}
