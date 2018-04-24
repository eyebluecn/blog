package cn.eyeblue.blog.rest.support.validation;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportValidationDao extends BaseEntityDao<SupportValidation> {


    SupportValidation findByCodeAndType(String code, SupportValidation.Type validation);

}
