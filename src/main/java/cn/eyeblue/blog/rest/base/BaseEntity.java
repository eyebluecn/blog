package cn.eyeblue.blog.rest.base;

import cn.eyeblue.blog.util.DateUtil;
import cn.eyeblue.blog.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity extends Base implements Serializable {

    //blog10的前缀表示是blog 1.0.x版本对应的数据库表。当数据库结构发生变化的时候，就不是小更新了。
    public final static String PREFIX = "blog10_";

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    public long sort = System.currentTimeMillis();

    //创建时间
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    @CreationTimestamp
    public Date createTime = new Date();

    //更新时间
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    @UpdateTimestamp
    public Date updateTime = new Date();


    @JsonIgnore
    public Boolean deleted = false;



}
