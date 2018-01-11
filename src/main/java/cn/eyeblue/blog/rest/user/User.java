package cn.eyeblue.blog.rest.user;

import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.tank.Tank;
import cn.eyeblue.blog.util.DateUtil;
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

    //邮箱是否已经验证
    private Boolean emailValidate = false;

    @Transient
    private Tank avatar;

    //总共的文章数量
    @Transient
    private int articleNum;

    //总共收到的点赞数
    @Transient
    private long articleAgreeNum = 0;

    //总共文章字数
    @Transient
    private long articleWords = 0;

    //总阅读量
    @Transient
    private long articleHit = 0;

    //总共收到的评论
    @Transient
    private long commentNum = 0;


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
        //游客角色不会持久化到数据库
        GUEST,
        //普通注册用户
        USER,
        //管理员
        ADMIN
    }

}


