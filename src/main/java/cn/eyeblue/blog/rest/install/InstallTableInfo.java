package cn.eyeblue.blog.rest.install;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class InstallTableInfo {

    private String name;
    private boolean tableExist;
    private List<String> allFields;
    private List<String> missingFields;

}
