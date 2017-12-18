package cn.zicla.blog.rest.comment;

import cn.zicla.blog.rest.base.BaseEntity;
import cn.zicla.blog.rest.base.Pager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Transient;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Comment extends BaseEntity {

    //文章uui
    private String articleUuid;

    //创建者,如果登录了用户才有。
    private String userUuid;

    //是否是楼层评论
    private Boolean isFloor;

    //如果不是楼层评论，那么应该附着的楼层uuid
    private String floorUuid;

    //回复的uuid
    private String puuid;

    //评论者姓名
    private String name;

    //评论者邮箱
    private String email;

    //评论内容
    private String content;

    //是否被举报
    private Boolean isReport;

    //举报内容
    private String report;


    //如果当前comment是一个floor，那么它下面的pager.
    @Transient
    private Pager<Comment> commentPager;


}


