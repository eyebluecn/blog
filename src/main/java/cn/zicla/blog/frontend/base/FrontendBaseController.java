package cn.zicla.blog.frontend.base;

import cn.zicla.blog.rest.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class FrontendBaseController extends BaseController {

    protected String templateUrl = null;

}
