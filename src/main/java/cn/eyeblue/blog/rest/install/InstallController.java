package cn.eyeblue.blog.rest.install;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.DataSourceConfiguration;
import cn.eyeblue.blog.rest.base.BaseController;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/install")
public class InstallController extends BaseController {

    @Autowired
    InstallService installService;


    //验证数据库连接是否有效
    @RequestMapping("/ping")
    @Feature(FeatureType.PUBLIC)
    public WebResult ping(@Valid InstallForm installForm) {

        Install install = installForm.create();

        installService.ping(install);

        return success();
    }


    //获取表的信息
    @RequestMapping("/table/info/list")
    @Feature(FeatureType.PUBLIC)
    public WebResult tableInfoList(@Valid InstallForm installForm) {

        Install install = installForm.create();

        List<InstallTableInfo> tableInfo = installService.getTableInfo(install);

        return successObject(tableInfo);
    }


    //完成安装
    @RequestMapping("/finish")
    @Feature(FeatureType.PUBLIC)
    public WebResult finish() {

        DataSource bean = AppContextManager.getBean(DataSource.class);

        DataSourceConfiguration routingDataSource = AppContextManager.getBean(DataSourceConfiguration.class);
        routingDataSource.addDataSource();

        return success("hello " + new Date() + " " + bean);
    }


}
