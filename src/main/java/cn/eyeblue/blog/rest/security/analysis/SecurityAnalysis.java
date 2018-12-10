package cn.eyeblue.blog.rest.security.analysis;

import cn.eyeblue.blog.rest.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class SecurityAnalysis extends BaseEntity {

    private Date dt;
    private long pv;
    private long uv;
    private long userNum;
    private long newUserNum;
    private long amount;

    public SecurityAnalysis(
            Date dt,
            long pv,
            long uv,
            long userNum,
            long newUserNum,
            long amount
    ) {
        this.dt = dt;
        this.pv = pv;
        this.uv = uv;
        this.userNum = userNum;
        this.newUserNum = newUserNum;
        this.amount = amount;
    }

    public void update(
            Date dt,
            long pv,
            long uv,
            long userNum,
            long newUserNum,
            long amount
    ) {
        this.dt = dt;
        this.pv = pv;
        this.uv = uv;
        this.userNum = userNum;
        this.newUserNum = newUserNum;
        this.amount = amount;
    }


}


