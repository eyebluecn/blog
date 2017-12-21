package cn.eyeblue.blog.rest.comment;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import cn.eyeblue.blog.rest.comment.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends BaseEntityDao<Comment> {

    int countByArticleUuidAndDeletedFalse(String articleUuid);
}
