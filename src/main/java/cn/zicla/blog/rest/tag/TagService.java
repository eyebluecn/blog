package cn.zicla.blog.rest.tag;

import cn.zicla.blog.rest.base.BaseEntityService;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TagService extends BaseEntityService<Tag> {

    @Autowired
    TagDao userDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    public TagService() {
        super(Tag.class);
    }


}
