package cn.zicla.blog.frontend.index;

import cn.zicla.blog.frontend.base.FrontendBaseController;
import cn.zicla.blog.frontend.home.HomeController;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        return homeController.index(model, request, response);
    }

}
