package cn.zicla.blog.rest.support.captcha;

import cn.zicla.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportCaptchaDao extends BaseEntityDao<SupportCaptcha> {

    SupportCaptcha findBySessionId(String sessionId);
}
