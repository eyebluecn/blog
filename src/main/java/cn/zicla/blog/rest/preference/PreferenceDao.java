package cn.zicla.blog.rest.preference;

import cn.zicla.blog.rest.base.BaseEntityDao;
import cn.zicla.blog.rest.preference.Preference;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceDao extends BaseEntityDao<Preference> {

    Preference findTopByDeletedFalse();

}
