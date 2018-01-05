package cn.eyeblue.blog.rest.support.validation;

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
public class SupportValidation extends BaseEntity {

    //过期：24h
    public static int EXPIRE_INTERVAL = 24 * 60 * 60 * 1000;

    private String userUuid;

    private String email;
    private String code;

    //类型
    @Enumerated(EnumType.STRING)
    private Type type = Type.VALIDATION;


    public enum Type {
        //验证邮箱
        VALIDATION,
        //找回密码
        FIND
    }


}


