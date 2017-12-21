package cn.eyeblue.blog.rest.support.captcha;


import cn.eyeblue.blog.rest.base.BaseController;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import com.github.cage.Cage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/support/captcha")
public class SupportCaptchaController extends BaseController {

    @Autowired
    SupportCaptchaDao supportCaptchaDao;


    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/fetch")
    public void fetch(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String sessionId = request.getSession().getId();
        Cage cage = new Cage();

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        String captchaValue = RandomStringUtils.random(4, true, true);

        SupportCaptcha supportCaptcha = supportCaptchaDao.findBySessionId(sessionId);

        if (supportCaptcha == null) {
            supportCaptcha = new SupportCaptcha(sessionId, captchaValue);
        } else {
            supportCaptcha.update(captchaValue);
        }
        supportCaptchaDao.save(supportCaptcha);

        cage.draw(captchaValue, response.getOutputStream());
    }


}
