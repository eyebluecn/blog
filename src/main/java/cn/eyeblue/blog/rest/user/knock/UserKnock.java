package cn.eyeblue.blog.rest.user.knock;

import cn.eyeblue.blog.rest.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class UserKnock extends BaseEntity {

    private String sessionId;
    private String userUuid;
    private String username;
    private String password;
    private String ip;
    private String address;

    @Enumerated(EnumType.STRING)
    private Type type = Type.OK;

    //类型
    public enum Type {
        //登录正常
        OK,
        //密码错误
        PASSWORD_ERROR,
        //验证码错误
        CAPTCHA_ERROR,
        //没有该用户名
        NO_SUCH_USERNAME
    }


    public UserKnock(String sessionId, String userUuid, String username, String password, String ip, UserKnock.Type type) {
        this.sessionId = sessionId;
        this.userUuid = userUuid;
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.type = type;
    }

}


