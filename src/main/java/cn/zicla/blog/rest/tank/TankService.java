package cn.zicla.blog.rest.tank;

import cn.zicla.blog.config.Config;
import cn.zicla.blog.config.exception.UtilException;
import cn.zicla.blog.rest.base.BaseEntityService;
import cn.zicla.blog.rest.tank.remote.TankMessage;
import cn.zicla.blog.util.JsonUtil;
import lombok.Getter;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class TankService extends BaseEntityService<Tank> {

    public static final Logger logger = LoggerFactory.getLogger(TankService.class);
    @Autowired
    private Config config;

    @Autowired
    TankDao tankDao;

    @Getter
    @Value("${tank.host}")
    private String tankHost;

    @Getter
    @Value("${tank.email}")
    private String tankEmail;

    @Getter
    @Value("${tank.password}")
    private String tankPassword;


    public TankService() {
        super(Tank.class);
    }

    private TankMessage getToken(String url, List<NameValuePair> nvps){
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        } catch (UnsupportedEncodingException e) {
            throw new UtilException(e);
        }

        TankMessage tankMessage = null;
        try (CloseableHttpResponse response1 = httpclient.execute(httpPost)) {
            HttpEntity entity1 = response1.getEntity();
            String json = EntityUtils.toString(entity1);
            logger.debug(json);
            EntityUtils.consume(entity1);
            tankMessage = JsonUtil.toObject(json, TankMessage.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UtilException("无法连接到资源服务器，请稍后再试！");
        }

        if (tankMessage == null) {
            throw new UtilException("资源服务器返回异常结果，请稍后再试！");
        }

        return tankMessage;

    }

    //从远程去获取uploadToken.
    public Tank httpFetchUploadToken() {

        return null;
    }

    //从远程去获取downloadToken
    public String httpFetchDownloadToken() {

        return null;
    }
}
