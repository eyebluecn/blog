package cn.zicla.blog.rest.user;

import cn.zicla.blog.rest.base.BaseEntity;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.tank.Tank;
import cn.zicla.blog.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    public static final String TAG = "user";

    private String username;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String email;

    private String phone;

    //用户角色
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.UNKNOWN;

    private String city;

    private String description;

    private String avatarTankUuid;

    private String avatarUrl;

    //上次登录IP
    @JsonIgnore
    private String lastIp;

    //上次登录时间
    @JsonIgnore
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    private Date lastTime;

    @Transient
    private Tank avatar;

    public boolean hasPermission(FeatureType featureType) {

        if (this.role == Role.ADMIN) {
            return true;
        } else if (this.role == Role.USER) {
            return featureType == FeatureType.PUBLIC || featureType == FeatureType.USER_MINE;
        } else {
            return featureType == FeatureType.PUBLIC;
        }

    }

    //性别
    public enum Gender {
        UNKNOWN,
        MALE,
        FEMALE
    }

    //角色
    public enum Role {
        USER,
        ADMIN
    }

}


