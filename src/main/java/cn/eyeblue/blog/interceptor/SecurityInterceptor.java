package cn.eyeblue.blog.interceptor;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.Config;
import cn.eyeblue.blog.config.exception.ServerException;
import cn.eyeblue.blog.rest.common.AsyncHandlerService;
import cn.eyeblue.blog.rest.common.DingdingService;
import cn.eyeblue.blog.rest.security.visit.SecurityVisit;
import cn.eyeblue.blog.rest.security.visit.SecurityVisitService;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.util.JsonUtil;
import cn.eyeblue.blog.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


//监控（请求时间，请求频率）拦截器
@Slf4j
public class SecurityInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<>("startTimeThreadLocal");
    private static final int MAX_TIME = 1000;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //标记开始计时时间
        startTimeThreadLocal.set(System.currentTimeMillis());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }


    //preHandle被拦截掉的请求，不会再进入该方法。
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        long beginTime = startTimeThreadLocal.get();
        long endTime = System.currentTimeMillis();

        User user = AppContextManager.findUser();
        String userUuid = null;
        if (user != null) {
            userUuid = user.getUuid();
        }

        String ip = NetworkUtil.getIpAddress(request);
        String host = NetworkUtil.getHost(request);
        String uri = request.getRequestURI();
        //这个map不能修改
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String[]> map = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (Objects.equals(entry.getKey(), "password")) {
                map.put(entry.getKey(), new String[]{"******"});
            } else {
                map.put(entry.getKey(), entry.getValue());
            }
        }

        String params = JsonUtil.toJson(map);
        long cost = endTime - beginTime;

        //落请求日志。
        AsyncHandlerService asyncHandlerService = AppContextManager.getBean(AsyncHandlerService.class);
        String finalUserUuid = userUuid;
        asyncHandlerService.submit(() -> {

            SecurityVisitService securityVisitService = AppContextManager.getBean(SecurityVisitService.class);
            SecurityVisit securityVisit = new SecurityVisit(
                    finalUserUuid,
                    ip,
                    host,
                    uri,
                    params,
                    cost
            );
            securityVisitService.save(securityVisit);

        });


        Config config = AppContextManager.getBean(Config.class);
        //如果请求时间过长，立即报警。
        if (cost > MAX_TIME) {

            List<String> whiteList = new ArrayList<String>() {{
                add("/weixin/callback/login");
                add("/api/activity/bill/download/xls");
            }};
            if (whiteList.contains(uri)) {

                log.info("{} 请求虽然超时 {} > {}，但是在白名单中，不钉钉告警", uri, cost, MAX_TIME);
            } else {
                DingdingService dingdingService = AppContextManager.getBean(DingdingService.class);
                dingdingService.sendMvcExceptionInfo(new ServerException("请求超时 {} > {}", cost, MAX_TIME));
            }


        }

    }
}
