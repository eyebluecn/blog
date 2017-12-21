package cn.eyeblue.blog.rest.comment;

import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.BaseEntityForm;
import cn.eyeblue.blog.rest.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = false)
@Data
public class CommentForm extends BaseEntityForm<Comment> {

    @NotNull
    private String articleUuid;

    //是否是楼层评论
    @NotNull
    private Boolean isFloor;

    //如果不是楼层评论，那么应该附着的楼层uuid
    private String floorUuid;

    //回复的uuid
    private String puuid;

    //评论者姓名
    @NotNull
    private String name;

    //评论者邮箱
    @NotNull
    private String email;

    //评论内容
    @NotNull
    private String content;

    public CommentForm() {
        super(Comment.class);
    }

    @Override
    protected void update(Comment comment, User operator) {

        comment.setArticleUuid(articleUuid);

        comment.setIsFloor(isFloor);
        if (!isFloor && (floorUuid == null || floorUuid.equals(""))) {
            throw new UtilException("floorUuid必填");
        }
        comment.setFloorUuid(floorUuid);

        if (!isFloor && (puuid == null || puuid.equals(""))) {
            throw new UtilException("puuid必填");
        }
        comment.setPuuid(puuid);

        comment.setName(name);
        comment.setEmail(email);
        comment.setContent(content);

    }


}

