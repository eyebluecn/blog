package cn.eyeblue.blog.rest.common;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.Config;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.util.JsonUtil;
import cn.eyeblue.blog.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//所有钉钉消息的发送均带有异步功能。
@Service
@Slf4j
public class DingdingService {

    @Autowired
    Config config;


    @Autowired
    AsyncHandlerService asyncHandlerService;


    //由于获取localhost的速度非常慢，因此我们在应用启动后就异步去获取一下localhost.
    @EventListener(ContextRefreshedEvent.class)
    public void updateLocalhost() {
        asyncHandlerService.submit(() -> {
            String localHost = NetworkUtil.getLocalHost();
            log.info("本机名称：{}", localHost);
        });
    }

    //发送http请求触发的异常信息，这里自动处理了异步发送的逻辑。
    public void sendMvcExceptionInfo(Throwable throwable) {

        HttpServletRequest request = NetworkUtil.getRequest();
        HttpSession httpSession = request.getSession(true);
        Object userObject = httpSession.getAttribute(User.TAG);
        String userInfo = "【未登录】" + NetworkUtil.getIpAddress(request);
        if (userObject instanceof User) {
            User user = (User) userObject;
            userInfo = "【" + user.getNickname() + "】" + user.getUuid();
        }

        String content = "应用：" + config.getAppName() + "\n" +
                "主机：" + NetworkUtil.getLocalHost() + "\n" +
                "触发：Http请求\n" +
                "接口：" + NetworkUtil.getHost(request) + NetworkUtil.getCurrentURI(request) + "\n" +
                "参数：" + JsonUtil.toJson(request.getParameterMap()) + "\n" +
                "用户：" + userInfo + "\n" +
                "原因：" + ExceptionUtils.getRootCauseMessage(throwable) + "\n";

        //注意：发送钉钉本身如果报错了，就不能再用钉钉去通知了，否则就是无穷循环
        asyncHandlerService.submit(() -> send(content), false);
    }

    //发送异步任务触发的异常信息，这里自动处理了异步发送的逻辑。
    public void sendHandlerExceptionInfo(Throwable throwable) {
        String content = "应用：" + config.getAppName() + "\n" +
                "触发：异步任务或回调函数\n" +
                "主机：" + NetworkUtil.getLocalHost() + "\n" +
                "原因：" + ExceptionUtils.getRootCauseMessage(throwable) + "\n";

        //注意：发送钉钉本身如果报错了，就不能再用钉钉去通知了，否则就是无穷循环
        asyncHandlerService.submit(() -> send(content), false);
    }


    private void send(String content) {

        String dingdingUrl = AppContextManager.getBean(Config.class).getAliDingdingUrl();

        HttpClient httpclient = HttpClients.createDefault();

        HttpPost httppost = new HttpPost(dingdingUrl);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");


        String safeContent = content.replace("\"", "'");
        String textMsg = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"" + safeContent + "\"}}";
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);

        log.info("发送钉钉消息：{}", textMsg);

        try {
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                log.info("发送钉钉消息状态：{}", result);
            } else {
                log.info("提交发送请求失败");
            }


        } catch (Throwable throwable) {
            log.error("发送钉钉消息时出错，{}", throwable.getMessage());
        }


    }


}
