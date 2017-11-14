package cn.zicla.blog.rest.base;

import cn.zicla.blog.util.DateUtil;
import cn.zicla.blog.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@MappedSuperclass
public class BaseEntity {

    public final static String PREFIX = "blog_";

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


    /**
     * 极简模式，只返回关键数据。一般用于 小权限数据，或者是为了加快接口速度。
     */
    public Map<String, Object> simpleMap() {
        return new HashMap<>();
    }


    public Map<String, Object> map() {

        return JsonUtil.toMap(this);
    }


    public Map<String, Object> detailMap() {

        return this.map();
    }


}
