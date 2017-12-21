package cn.eyeblue.blog.rest.preference;

import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.support.session.SupportSessionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PreferenceService extends BaseEntityService<Preference> {

    @Autowired
    PreferenceDao preferenceDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    public PreferenceService() {
        super(Preference.class);
    }

    public Preference fetch() {

        Preference preference = preferenceDao.findTopByDeletedFalse();
        if (preference == null) {
            preference = Preference.create();
            preferenceDao.save(preference);
        }

        return preference;
    }


}
