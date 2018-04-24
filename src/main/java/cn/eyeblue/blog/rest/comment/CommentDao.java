package cn.eyeblue.blog.rest.comment;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends BaseEntityDao<Comment> {

    int countByArticleUuid(String articleUuid);
}
