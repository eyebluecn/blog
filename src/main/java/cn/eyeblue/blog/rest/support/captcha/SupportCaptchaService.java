package cn.eyeblue.blog.rest.support.captcha;

import cn.eyeblue.blog.config.Config;
import cn.eyeblue.blog.rest.base.BaseEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SupportCaptchaService extends BaseEntityService<SupportCaptcha> {

    @Autowired
    SupportCaptchaDao supportCaptchaDao;

    @Autowired
    Config config;

    public SupportCaptchaService() {
        super(SupportCaptcha.class);
    }

    public boolean validate(String sessionId, String captchaValue) {

        //如果是测试环境，允许5555直接通过。
        if (config.isDebug() && "5555".equals(captchaValue)) {
            return true;
        }

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
