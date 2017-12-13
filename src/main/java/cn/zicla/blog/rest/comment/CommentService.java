package cn.zicla.blog.rest.comment;

import cn.zicla.blog.rest.base.BaseEntityService;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommentService extends BaseEntityService<Comment> {

    @Autowired
    CommentDao userDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    public CommentService() {
        super(Comment.class);
    }


}
