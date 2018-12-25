package cn.eyeblue.blog.interceptor;

import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CustomErrorController implements ErrorController {

    @Feature(FeatureType.PUBLIC)
    @RequestMapping("/error")
    public String handleError(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        model.addAttribute("statusCode", statusCode);
        return this.getErrorPath();
    }

    @Override
    public String getErrorPath() {
        return "error/index";
    }

}
