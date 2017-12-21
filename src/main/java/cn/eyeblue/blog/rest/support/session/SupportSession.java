package cn.eyeblue.blog.rest.support.session;

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
public class SupportSession extends BaseEntity {

    //默认记住一周时间(s)
    public final static int EXPIRY = 60 * 60 * 24 * 7;


    private String userUuid;

    private String ip;

    //上次登录时间
    @JsonIgnore
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    private Date expireTime;

    public SupportSession(String userUuid, String ip, Date expireTime) {
        this.userUuid = userUuid;
        this.ip = ip;
        this.expireTime = expireTime;
    }


}


