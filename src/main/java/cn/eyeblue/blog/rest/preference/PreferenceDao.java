package cn.eyeblue.blog.rest.preference;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceDao extends BaseEntityDao<Preference> {

    Preference findTopByOrderByCreateTime();


}
