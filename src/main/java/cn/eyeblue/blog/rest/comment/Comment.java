package cn.eyeblue.blog.rest.comment;

import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.base.Pager;
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
    private Boolean isFloor = true;

    //如果不是楼层评论，那么应该附着的楼层uuid
    private String floorUuid;

    //回复的uuid
    private String puuid;

    //评论者姓名
    private String name;

    //评论者头像，只有站内的用户才有。
    private String avatarUrl;

    //评论者邮箱
    private String email;

    //评论内容
    private String content;

    //点赞
    private Integer agree = 0;

    //评论时的ip
    private String ip;


    //用户对于这个评论是否已经点赞了。
    @Transient
    private boolean agreed = false;

    //如果当前comment是一个floor，那么它下面的pager.
    @Transient
    private Pager<Comment> commentPager;


    //评论对应的文章名称
    @Transient
    private String articleTitle;


    //是否被举报过，在评论详情中有用到。
    @Transient
    private Boolean isReport;


}


