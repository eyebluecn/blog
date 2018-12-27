package cn.eyeblue.blog.rest.install;

import cn.eyeblue.blog.rest.base.BaseController;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/install")
public class InstallController extends BaseController {


    //获取。
    @RequestMapping("/hello")
    @Feature(FeatureType.PUBLIC)
    public WebResult hello() {


        return success("hello " + new Date());
    }


}
