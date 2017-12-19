package cn.zicla.blog.rest.agree;

import cn.zicla.blog.rest.base.BaseEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AgreeService extends BaseEntityService<Agree> {

    @Autowired
    AgreeDao userDao;


    public AgreeService() {
        super(Agree.class);
    }

}
