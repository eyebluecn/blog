package cn.eyeblue.blog.lobby.index;

import cn.eyeblue.blog.lobby.base.FrontendBaseController;
import cn.eyeblue.blog.lobby.home.HomeController;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController extends FrontendBaseController {

    @Autowired
    HomeController homeController;

    /**
     * 自动转跳到/home/index去。
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/")
    public String index(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "15") Integer pageSize
    ) {
        return homeController.index(model, request, response, page, pageSize);
    }

}
