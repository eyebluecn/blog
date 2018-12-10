package cn.eyeblue.blog.util;

import cn.eyeblue.blog.rest.base.WebResult;
import lombok.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class NetworkUtil {


    //缓存主机名
    public static String serverName = null;


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     * 如果使用了Nginx，getRemoteAddr() 将永远是 127.0.0.1 没有意义。
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");


        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");

            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");

            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");

            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");

            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();

            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String ip1 : ips) {
                if (!("unknown".equalsIgnoreCase(ip1))) {
                    ip = ip1;
                    break;
                }
            }
        }
        return ip;
    }

    public static String getIpAddress() {
        return getIpAddress(getRequest());
    }

    //获取对方请求的host
    public static String getHost(HttpServletRequest request) {

        String schema = request.getScheme();
        String headerHost = request.getHeader("Host");
        if (StringUtil.isEmpty(headerHost)) {

            String remoteHost = request.getRemoteHost();
            int remotePort = request.getRemotePort();

            if (remotePort == 80) {
                return schema + "://" + remoteHost;
            } else {
                return schema + "://" + remoteHost + ":" + remotePort;
            }
        } else {
            return schema + "://" + headerHost;
        }

    }

    public static String getHost() {
        return getHost(getRequest());
    }

    //获取当前请求的url 不包含Host，类似于 /api/user/login
    public static String getCurrentURI() {
        HttpServletRequest request = getRequest();
        return getCurrentURI(request);
    }

    public static String getCurrentURI(HttpServletRequest request) {
        return request.getRequestURI();
    }


    //获取到当前这个链接的的request
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return attributes.getRequest();
    }


    //获取本台机器的host
    public static String getLocalHost() {

        if (serverName == null) {
            try {
                serverName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ex) {
                serverName = "Unknown Server Name";
            }
        }

        return serverName;
    }


    //从request中获取用户登录的token
    public static String getAuthentication(@NonNull HttpServletRequest request) {

        //尝试去验证token。
        Cookie[] cookies = request.getCookies();
        String authentication = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (WebResult.COOKIE_AUTHENTICATION.equals(cookie.getName())) {
                    authentication = cookie.getValue();
                }
            }
        }

        //尝试从request中读取
        if (authentication == null) {
            authentication = request.getParameter(WebResult.COOKIE_AUTHENTICATION);
        }

        return authentication;
    }
}