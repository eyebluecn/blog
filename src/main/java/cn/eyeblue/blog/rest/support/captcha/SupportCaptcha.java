package cn.eyeblue.blog.rest.support.captcha;

import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class SupportCaptcha extends BaseEntity {

    //过期：2min
    public static int EXPIRE_INTERVAL = 2 * 60 * 1000;

    private String sessionId;

    private String value;

    //上次登录时间
    @JsonIgnore
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    private Date expireTime;

    public SupportCaptcha(String sessionId, String value) {
        this.sessionId = sessionId;
        this.value = value;
        this.expireTime = new Date(System.currentTimeMillis() + EXPIRE_INTERVAL);
    }

    public void update(String value) {
        this.setValue(value);
        this.setExpireTime(new Date(System.currentTimeMillis() + SupportCaptcha.EXPIRE_INTERVAL));
    }

}


