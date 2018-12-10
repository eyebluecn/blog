package cn.eyeblue.blog.rest.tank;

import cn.eyeblue.blog.config.Config;
import cn.eyeblue.blog.config.exception.BadRequestException;
import cn.eyeblue.blog.config.exception.ServerException;
import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.tank.remote.*;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.util.DateUtil;
import cn.eyeblue.blog.util.JsonUtil;
import cn.eyeblue.blog.util.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Service
public class TankService extends BaseEntityService<Tank> {

    @Autowired
    private Config config;

    @Autowired
    TankDao tankDao;

    @Getter
    @Value("${tank.url}")
    private String tankUrl;

    @Getter
    @Value("${tank.email}")
    private String tankEmail;

    @Getter
    @Value("${tank.password}")
    private String tankPassword;

    private final static String URL_FETCH_UPLOAD_TOKEN = "/api/alien/fetch/upload/token";
    private final static String URL_FETCH_DOWNLOAD_TOKEN = "/api/alien/fetch/download/token";
    private final static String URL_CONFIRM = "/api/alien/confirm";
    private final static String URL_UPLOAD = "/api/alien/upload";
    private final static String URL_CRAWL_TOKEN = "/api/alien/crawl/token";
    private final static String URL_CRAWL_DIRECT = "/api/alien/crawl/direct";
    private final static String URL_DOWNLOAD = "/api/alien/download";

    public TankService() {
        super(Tank.class);
    }

    private <T extends TankBaseEntity> TankMessage<T> doPost(String url, Map<String, String> params, TypeReference<TankMessage<T>> typeReference) {

        List<NameValuePair> nvps = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ServerException(e);
        }

        TankMessage<T> tankMessage = null;
        try (CloseableHttpResponse response1 = httpclient.execute(httpPost)) {
            HttpEntity entity1 = response1.getEntity();
            String json = EntityUtils.toString(entity1, "UTF-8");
            log.info(json);

            EntityUtils.consume(entity1);

            tankMessage = JsonUtil.toGenericObject(typeReference, json);
        } catch (Throwable e) {
            log.error(StringUtil.format("连接蓝眼云盘出错 url:{},params:{}", url, JsonUtil.toJson(params)), e);
            String message = e.getMessage();
            if (message == null || message.equals("")) {
                message = "无法连接到资源服务器，请稍后再试！";
            }
            throw new ServerException(message);
        }

        if (tankMessage == null) {
            throw new ServerException("资源服务器返回异常结果，请稍后再试！");
        }

        return tankMessage;

    }

    //我们存放在tank服务器的文件资源按照如下的命名规则： /app/blog/yyyy/MM/dd/timestamp
    private String getStoreDir() {
        Date date = new Date();
        String dateString = DateUtil.convertDateToString(date, "/yyyy/MM/dd");
        return "/app/erhua" + dateString + "/" + date.getTime();
    }

    //从远程去获取uploadToken.
    public Tank httpFetchUploadToken(String filename, boolean privacy, long size, User operator) {

        TankMessage<UploadToken> tankMessage = this.doPost(this.tankUrl + URL_FETCH_UPLOAD_TOKEN, new HashMap<String, String>() {{
            put("email", TankService.this.tankEmail);
            put("password", TankService.this.tankPassword);
            put("filename", filename);
            put("privacy", "" + privacy);
            put("size", "" + size);
            put("dir", TankService.this.getStoreDir());
        }}, new TypeReference<TankMessage<UploadToken>>() {
        });


        if (tankMessage.getCode() != WebResultCode.OK) {
            throw new ServerException(tankMessage.getMsg());
        }

        //如果获取uploadToken成功的话，那么就直接创建一个tank文件了。
        UploadToken uploadToken = tankMessage.getData();
        Tank tank = new Tank(operator.getUuid(), uploadToken.getFilename(), uploadToken.getSize(), uploadToken.isPrivacy());

        tank = tankDao.save(tank);

        //附上用户上传需要的内容。
        tank.setUploadTokenUuid(uploadToken.getUuid());
        tank.setUploadUrl(tankUrl + URL_UPLOAD);
        return tank;
    }


    //通过一个url，交给远程去爬取文件。
    public Tank httpCrawlDirect(String url, User operator, String filename) {

        log.info("远程爬取文件：{}", url);
        if (StringUtils.isEmpty(filename)) {
            URL urlObj = null;
            try {
                urlObj = new URL(url);
            } catch (MalformedURLException e) {
                throw new BadRequestException("指定的url格式错误");
            }
            filename = FilenameUtils.getName(urlObj.getPath());
        }

        String finalFilename = filename;
        TankMessage<Matter> tankMessage = this.doPost(this.tankUrl + URL_CRAWL_DIRECT, new HashMap<String, String>() {{
            put("email", TankService.this.tankEmail);
            put("password", TankService.this.tankPassword);
            put("filename", finalFilename);
            put("privacy", "false");
            put("url", url);
            put("dir", TankService.this.getStoreDir());
        }}, new TypeReference<TankMessage<Matter>>() {
        });

        if (tankMessage.getCode() != WebResultCode.OK) {
            throw new ServerException(tankMessage.getMsg());
        }


        //如果抓取成功的话，那么就直接创建一个tank文件了。
        Matter matter = tankMessage.getData();
        if (matter == null || StringUtils.isEmpty(matter.getUuid())) {
            throw new ServerException("远程拉取文件失败");
        }

        Tank tank = new Tank(operator.getUuid(), matter.getName(), matter.getSize(), matter.isPrivacy());
        tank.setMatterUuid(matter.getUuid());
        tank.setUrl(this.tankUrl + URL_DOWNLOAD + "/" + matter.getUuid() + "/" + matter.getName());
        tank.setConfirmed(true);

        tank = tankDao.save(tank);

        return tank;
    }


    //从远程去确认文件
    public Tank httpConfirm(String uuid, @RequestParam String matterUuid) {

        Tank tank = this.check(uuid);
        if (tank.isConfirmed()) {
            throw new ServerException("文件已经被确认了，请勿重复操作。");
        }

        TankMessage<Matter> tankMessage = this.doPost(this.tankUrl + URL_CONFIRM, new HashMap<String, String>() {{
            put("email", TankService.this.tankEmail);
            put("password", TankService.this.tankPassword);
            put("matterUuid", matterUuid);
        }}, new TypeReference<TankMessage<Matter>>() {
        });
        if (tankMessage.getCode() != WebResultCode.OK) {
            throw new ServerException(tankMessage.getMsg());
        }


        Matter matter = tankMessage.getData();
        if (!tank.getName().equals(matter.getName())) {
            throw new ServerException("文件名不一致，确认失败。");
        }
        if (tank.getSize() != matter.getSize()) {
            throw new ServerException("文件大小不一致，确认失败。");
        }
        if (tank.getPrivacy() != matter.isPrivacy()) {
            throw new ServerException("文件公开性不一致，确认失败。");
        }

        tank.setMatterUuid(matterUuid);
        tank.setConfirmed(true);

        String name = tank.getName();
        try {
            name = URLEncoder.encode(name, "utf-8");
        } catch (Exception e) {
            //ignore
        }

        tank.setUrl(this.tankUrl + URL_DOWNLOAD + "/" + matterUuid + "/" + name);

        tankDao.save(tank);

        return tank;
    }

    //从远程去获取downloadToken
    public String httpFetchDownloadUrl(String uuid) {

        Tank tank = this.check(uuid);
        if (!tank.isConfirmed()) {
            throw new ServerException("该文件尚未被确认，无法下载！");
        }

        if (!tank.getPrivacy()) {
            return tank.getUrl();
        }


        TankMessage<DownloadToken> tankMessage = this.doPost(this.tankUrl + URL_FETCH_DOWNLOAD_TOKEN, new HashMap<String, String>() {{
            put("email", TankService.this.tankEmail);
            put("password", TankService.this.tankPassword);
            put("matterUuid", tank.getMatterUuid());
            put("expire", 86400 + "");
        }}, new TypeReference<TankMessage<DownloadToken>>() {
        });
        if (tankMessage.getCode() != WebResultCode.OK) {
            throw new ServerException(tankMessage.getMsg());
        }


        return tank.getUrl() + "?downloadTokenUuid=" + tankMessage.getData().getUuid();
    }


    //从一个json数组字符串中去获取出Tank数组。
    public List<Tank> getTanksByUuidsJson(String uuidsJson) {

        if (StringUtils.isEmpty(uuidsJson)) {
            return new ArrayList<>();
        }

        List<String> uuids = JsonUtil.toStringList(uuidsJson);

        return getTanksByUuids(uuids);
    }

    public List<Tank> getTanksByUuids(List<String> uuids) {

        List<Tank> list = new ArrayList<>();
        Iterable<Tank> tanks = tankDao.findAllById(uuids);

        //为了保证tank和uuid的顺序一致。
        uuids.forEach(uuid -> {
            tanks.forEach(tank -> {
                if (uuid.equals(tank.getUuid())) {
                    list.add(tank);
                }
            });
        });

        return list;
    }


}
