package cn.zicla.blog.frontend.home;

import cn.zicla.blog.frontend.base.FrontendBaseController;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/home")
public class HomeController extends FrontendBaseController {

    /**
     * 首页
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

        return "home/index";
    }


    /**
     * 关于我们
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/about")
    public String about(Model model, HttpServletRequest request, HttpServletResponse response) {

        return "home/about";
    }


}
