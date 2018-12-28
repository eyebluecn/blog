package cn.eyeblue.blog.rest.install;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Install {

    private int mysqlPort;
    private String mysqlHost;
    private String mysqlSchema;
    private String mysqlUsername;
    private String mysqlPassword;

}
