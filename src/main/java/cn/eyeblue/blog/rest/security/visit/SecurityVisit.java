package cn.eyeblue.blog.rest.security.visit;

import cn.eyeblue.blog.rest.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class SecurityVisit extends BaseEntity {

    private String userUuid;
    private String ip;
    private String host;
    private String uri;
    private String params;
    private long cost = 0;


    public SecurityVisit(
            String userUuid,
            String ip,
            String host,
            String uri,
            String params,
            long cost
    ) {
        this.userUuid = userUuid;
        this.ip = ip;
        this.host = host;
        this.uri = uri;
        this.params = params;
        this.cost = cost;
    }


}


