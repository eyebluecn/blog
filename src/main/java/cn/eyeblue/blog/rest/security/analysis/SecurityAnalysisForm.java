package cn.eyeblue.blog.rest.security.analysis;

import cn.eyeblue.blog.rest.base.BaseEntityForm;
import cn.eyeblue.blog.rest.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class SecurityAnalysisForm extends BaseEntityForm<SecurityAnalysis> {


    public SecurityAnalysisForm() {
        super(SecurityAnalysis.class);
    }

    @Override
    protected void update(SecurityAnalysis securityAnalysis, User operator) {

    }

}

