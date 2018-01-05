package cn.eyeblue.blog.rest.support.validation;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportValidationDao extends BaseEntityDao<SupportValidation> {


    SupportValidation findByCodeAndTypeAndDeleted(String code, SupportValidation.Type validation, boolean b);

}
