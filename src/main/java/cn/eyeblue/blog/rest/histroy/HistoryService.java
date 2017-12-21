package cn.eyeblue.blog.rest.histroy;

import cn.eyeblue.blog.rest.base.BaseEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HistoryService extends BaseEntityService<History> {

    @Autowired
    HistoryDao userDao;

    public HistoryService() {
        super(History.class);
    }

}
