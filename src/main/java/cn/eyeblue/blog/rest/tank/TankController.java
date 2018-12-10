package cn.eyeblue.blog.rest.tank;

import cn.eyeblue.blog.rest.base.BaseEntityController;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/tank")
public class TankController extends BaseEntityController<Tank, TankForm> {

    @Autowired
    TankService tankService;

    @Autowired
    private TankDao tankDao;

    public TankController() {
        super(Tank.class);
    }


    @RequestMapping("/fetch/upload/token")
    @Feature(FeatureType.USER_MINE)
    public WebResult uploadToken(
            @RequestParam String filename,
            @RequestParam Boolean privacy,
            @RequestParam Long size
    ) {

        Tank tank = tankService.httpFetchUploadToken(filename, privacy, size, checkUser());

        return success(tank);
    }


    @RequestMapping("/download/{uuid}")
    @Feature(FeatureType.USER_MINE)
    public void download(HttpServletResponse response, @PathVariable String uuid) throws Exception {


        String url = tankService.httpFetchDownloadUrl(uuid);

        response.sendRedirect(url);

    }


    @RequestMapping("/fetch/download/{uuid}")
    @Feature(FeatureType.USER_MINE)
    public WebResult fetchDownload(@PathVariable String uuid) throws Exception {

        String url = tankService.httpFetchDownloadUrl(uuid);

        return success(url);

    }

    //通过一个url直接上传到蓝眼云盘中。
    @RequestMapping("/crawl/direct")
    @Feature(FeatureType.USER_MINE)
    public WebResult crawlDirect(@RequestParam String url, @RequestParam(required = false) String filename) {

        Tank tank = tankService.httpCrawlDirect(url, checkUser(), filename);

        return success(tank);
    }


    @RequestMapping("/confirm")
    @Feature(FeatureType.USER_MINE)
    public WebResult confirm(@RequestParam String uuid, @RequestParam String matterUuid) {
        Tank tank = tankService.httpConfirm(uuid, matterUuid);
        return success(tank);
    }


}
