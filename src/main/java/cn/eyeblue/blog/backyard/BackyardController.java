package cn.eyeblue.blog.backyard;

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
public class BackyardController {

    /**
     * /by开头的请求全部交给index.html去处理，那里由vue-router负责。
     */
    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/by/**")
    public String index(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "15") Integer pageSize
    ) {
        System.out.println("匹配到了啥");
        System.out.println(request.getRequestURI());
        return "index";
    }

}
