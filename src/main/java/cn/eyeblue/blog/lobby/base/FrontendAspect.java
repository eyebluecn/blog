package cn.eyeblue.blog.lobby.base;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.lobby.home.HomeController;
import cn.eyeblue.blog.rest.preference.PreferenceService;
import cn.eyeblue.blog.rest.user.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
@Slf4j
public class FrontendAspect {


    @Pointcut("execution(public String cn.eyeblue.blog.lobby.*.*Controller.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void frontendPointcut() {
    }


    //设置home_test的菜单
    private void initHomeMenus(Model model, String currentUrl) {

    }

    /***********请求参数信息***********/

    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private static String[] getParameterNames(Method method) {
        return parameterNameDiscoverer.getParameterNames(method);
    }

    private void initRequestParams(JoinPoint joinPoint, Model model, HttpServletRequest request, HttpServletResponse response) {

        Object[] objects = joinPoint.getArgs();

        //添加@RequestParam中的参数.
        Map<String, Object> requestParams = new HashMap<>();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String[] parameterNames = getParameterNames(method);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterNames.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            String parameterName = parameterNames[i];

            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof RequestParam) {
                        requestParams.put(parameterName, objects[i]);
                    }
                }
            }
        }
        model.addAttribute("requestParams", requestParams);


        //设置templateURL /home/index 即 home/index 在前端pager中非常有用。
        String requestUri = request.getRequestURI();
        model.addAttribute("baseUrl", requestUri);


    }

    /***********用户登录信息***********/

    private void initLoginInfo(JoinPoint joinPoint, Model model, HttpServletRequest request, HttpServletResponse response) {

        //设置网站偏好信息。
        PreferenceService preferenceService = AppContextManager.getBean(PreferenceService.class);
        model.addAttribute("preference", preferenceService.fetch());

        //设置用户登录信息。
        Map<String, Object> sessionMap = new HashMap<>();
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute(User.TAG);
        if (user == null) {
            //没有登录便放置一个游客身份。
            user = new User();
            user.setRole(User.Role.GUEST);
        }

        sessionMap.put(User.TAG, user);
        model.addAttribute("session", sessionMap);

    }

    @Before("frontendPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        //获取Model参数
        Object[] objects = joinPoint.getArgs();
        if (objects == null || objects.length == 0 || !(objects[0] instanceof Model)) {
            throw new UtilException("第一个参数必须定义Model.");
        }
        Model model = (Model) objects[0];

        //获取Controller实体以及Request对象
        FrontendBaseController frontendBaseController = (FrontendBaseController) joinPoint.getTarget();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        String requestUri = request.getRequestURI();

        //设置templateUrl.
        frontendBaseController.templateUrl = requestUri.substring(1);

        //设置每个控制器的菜单。
        if (frontendBaseController instanceof HomeController) {
            this.initHomeMenus(model, requestUri);
        }

        //设置每个页面的请求参数。
        this.initRequestParams(joinPoint, model, request, response);

        //初始化用户登录的信息
        this.initLoginInfo(joinPoint, model, request, response);

    }


}
