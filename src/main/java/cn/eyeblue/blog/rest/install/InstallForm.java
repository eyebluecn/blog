package cn.eyeblue.blog.rest.install;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Data
public class InstallForm {


    @Positive
    private int mysqlPort;
    @NotNull
    private String mysqlHost;
    @NotNull
    private String mysqlSchema;
    @NotNull
    private String mysqlUsername;
    @NotNull
    private String mysqlPassword;


    protected void update(Install install) {

        install.setMysqlPort(mysqlPort);
        install.setMysqlHost(mysqlHost);
        install.setMysqlSchema(mysqlSchema);
        install.setMysqlUsername(mysqlUsername);
        install.setMysqlPassword(mysqlPassword);
    }

    public Install create() {

        Install install = new Install();
        this.update(install);
        return install;
    }


}

