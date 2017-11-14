package cn.zicla.blog.rest.user;

import cn.zicla.blog.rest.base.BaseEntityController;
import cn.zicla.blog.rest.base.WebResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;

@RestController
@RequestMapping("/user")
public class UserController extends BaseEntityController<User,UserForm> {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    public UserController() {
        super(User.class);
    }


    @RequestMapping("/page")
    public WebResult page(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Sort.Direction orderLastTime,
            @RequestParam(required = false) Sort.Direction orderSort,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "false") Boolean autoComplete
    ) {

        Sort sort = new Sort(Sort.Direction.ASC, User_.deleted.getName());

        if (orderSort != null) {
            sort = sort.and(new Sort(orderSort, User_.sort.getName()));
        }
        if (orderLastTime != null) {
            sort = sort.and(new Sort(orderLastTime, User_.lastTime.getName()));
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);
        return this.success(((root, query, cb) -> {
                    Predicate predicate = cb.equal(root.get(User_.deleted), false);

                    if (username != null) {
                        predicate = cb.and(predicate, cb.like(root.get(User_.username), "%" + username + "%"));
                    }
                    if (keyword != null) {

                        Predicate predicate1 = cb.like(root.get(User_.username), "%" + keyword + "%");
                        Predicate predicate2 = cb.like(root.get(User_.email), "%" + keyword + "%");

                        predicate = cb.and(predicate, cb.or(predicate1, predicate2));
                    }
                    return predicate;

                })
                , pageable,
                user -> {
                    if (autoComplete) {
                        return user.simpleMap();
                    } else {
                        return user.detailMap();
                    }
                });


    }

}
