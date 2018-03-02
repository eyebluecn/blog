package cn.eyeblue.blog.rest.tank;

import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.Base;
import cn.eyeblue.blog.rest.base.BaseEntityController;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

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

    /**
     * 按照分页的方式获取
     */
    @RequestMapping("/page")
    @Feature(FeatureType.USER_MANAGE)
    public WebResult page(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer pageSize,
            @RequestParam(required = false) Sort.Direction orderCreateTime,
            @RequestParam(required = false) Boolean privacy,
            @RequestParam(required = false) String name
    ) {

        Sort sort = new Sort(Sort.Direction.ASC, Tank_.deleted.getName());

        if (orderCreateTime != null) {
            sort = sort.and(new Sort(orderCreateTime, Tank_.createTime.getName()));
        }


        Pageable pageable = getPageRequest(page, pageSize, sort);
        return this.success(((root, query, cb) -> {
            Predicate predicate = cb.equal(root.get(Tank_.deleted), false);
            if (name != null) {
                predicate = cb.and(predicate, cb.like(root.get(Tank_.name), "%" + name + "%"));
            }
            if (privacy != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Tank_.privacy), privacy));
            }

            return predicate;

        }), pageable, Base::map);


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

        Optional<Tank> optionalTank = tankDao.findById(uuid);

        if (!optionalTank.isPresent()) {
            throw new UtilException("文件不存在或者已经被删除！");
        }
        Tank tank = optionalTank.get();
        if (tank.deleted) {
            throw new UtilException("文件不存在或者已经被删除！");
        }
        if (!tank.getPrivacy()) {
            throw new UtilException("访问链接错误！");
        }

        String url = tankService.httpFetchDownloadUrl(uuid);

        response.sendRedirect(url);

    }


    @RequestMapping("/confirm")
    @Feature(FeatureType.USER_MINE)
    public WebResult confirm(@RequestParam String uuid, @RequestParam String matterUuid) {
        Tank tank = tankService.httpConfirm(uuid, matterUuid);
        return success(tank);
    }


}
