package cn.eyeblue.blog.rest.security.visit;

import cn.eyeblue.blog.rest.base.BaseEntityForm;
import cn.eyeblue.blog.rest.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class SecurityVisitForm extends BaseEntityForm<SecurityVisit> {


    public SecurityVisitForm() {
        super(SecurityVisit.class);
    }

    @Override
    protected void update(SecurityVisit securityVisit, User operator) {

    }

}

