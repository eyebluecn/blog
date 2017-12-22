package cn.eyeblue.blog.rest.report;

import cn.eyeblue.blog.rest.base.BaseEntityForm;
import cn.eyeblue.blog.rest.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class ReportForm extends BaseEntityForm<Report> {

    public ReportForm() {
        super(Report.class);
    }

    @Override
    protected void update(Report report, User operator) {


    }


}

