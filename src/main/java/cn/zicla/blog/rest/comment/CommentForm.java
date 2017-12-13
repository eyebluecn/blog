package cn.zicla.blog.rest.comment;

import cn.zicla.blog.rest.base.BaseEntityForm;
import cn.zicla.blog.rest.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class CommentForm extends BaseEntityForm<Comment> {

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


    public CommentForm() {
        super(Comment.class);
    }

    @Override
    protected void update(Comment comment, User operator) {

        comment.setIsFloor(isFloor);
        comment.setFloorUuid(floorUuid);
        comment.setPuuid(puuid);
        comment.setName(name);
        comment.setEmail(email);
        comment.setContent(content);

    }

    public Comment create(User operator) {

        Comment entity = new Comment();
        this.update(entity, operator);
        entity.setUserUuid(operator.getUuid());
        return entity;
    }

}

