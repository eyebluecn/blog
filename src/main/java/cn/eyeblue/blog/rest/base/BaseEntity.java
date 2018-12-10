package cn.eyeblue.blog.rest.base;

import cn.eyeblue.blog.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class BaseEntity extends Base implements Serializable {


    //blog11的前缀表示是blog 2.0.x版本对应的数据库表。当数据库结构发生变化的时候，就不是小更新了。
    public final static String PREFIX = "blog20_";
    public final static String EMPTY_JSON_ARRAY = "[]";
    public final static String EMPTY_JSON_OBJECT = "{}";


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    protected String uuid;

    protected long sort = System.currentTimeMillis();

    //创建时间
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    @CreationTimestamp
    protected Date createTime = new Date();

    //更新时间
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    @UpdateTimestamp
    protected Date updateTime = new Date();


}
