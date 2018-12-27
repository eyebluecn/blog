package cn.eyeblue.blog.rest.user;

import cn.eyeblue.blog.config.exception.LoginException;
import cn.eyeblue.blog.config.exception.NotFoundException;
import cn.eyeblue.blog.config.exception.UnauthorizedException;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.article.ArticleDao;
import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.core.Feature;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.support.session.SupportSession;
import cn.eyeblue.blog.rest.support.session.SupportSessionDao;
import cn.eyeblue.blog.rest.support.session.SupportSessionService;
import cn.eyeblue.blog.rest.tank.TankService;
import cn.eyeblue.blog.util.NetworkUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class UserService extends BaseEntityService<User> {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.nickname}")
    private String adminNickname;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    UserDao userDao;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    TankService tankService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    SupportSessionDao supportSessionDao;


    @Autowired
    SupportSessionService supportSessionService;


    public UserService() {
        super(User.class);
    }

    //初始化一个超级管理员。
    public void initAdmin() {
        long count = userDao.count();
        if (count > 0) {
            return;
        }
        User user = new User();
        user.setUsername(adminUsername);
        user.setNickname(adminNickname);
        user.setEmail(adminEmail);
        user.setPassword(bCryptPasswordEncoder.encode(adminPassword));
        user.setRole(User.Role.ADMIN);

        userDao.save(user);

    }

    //获取用户详情
    public User detail(String uuid) {
        //准备用户详情。
        User user = this.check(uuid);

        Object obj = articleDao.analysisTotal(user.getUuid());

        user.setArticleNum(articleDao.countByUserUuidAndPrivacyFalse(uuid));

        if (obj != null) {
            Object[] list = (Object[]) obj;
            if (list.length > 0 && list[0] != null) {
                user.setArticleAgreeNum((Long) list[0]);
            }
            if (list.length > 1 && list[1] != null) {
                user.setArticleWords((Long) list[1]);
            }

            if (list.length > 2 && list[2] != null) {
                user.setArticleHit((Long) list[2]);
            }

            if (list.length > 3 && list[3] != null) {
                user.setCommentNum((Long) list[3]);
            }

        }


        user.setAvatar(tankService.find(user.getAvatarTankUuid()));

        return user;
    }


    //这里的cacheLoader纯粹用于满足guava的格式，我们采用手动加载，获取时使用getIfPresent
    private CacheLoader<String, User> cacheLoader = new CacheLoader<String, User>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public User load(String key) {
            return null;
        }
    };

    //缓存登录用户的信息的情况 SupportSessionUuid => User.
    private LoadingCache<String, User> cache = CacheBuilder.newBuilder().maximumSize(10000).build(cacheLoader);

    //从一个request中获取一个用户
    public User getCachedUser(@NonNull HttpServletRequest request) {

        String authentication = NetworkUtil.getAuthentication(request);
        if (authentication != null) {
            User user = this.cache.getIfPresent(authentication);

            if (user != null) {
                //同时没有过期
                if (user.getLastTime().getTime() + 1000 * SupportSession.EXPIRY < System.currentTimeMillis()) {
                    this.expireCache(authentication);

                    user = null;
                }
            }

            return user;
        } else {
            return null;
        }

    }

    //缓存一个用户信息
    public void cacheUser(@NonNull String supportSessionUuid, @NonNull User user) {
        log.info("缓存用户 key={} nickname={} phone={}", supportSessionUuid, user.getNickname(), user.getPhone());
        this.cache.put(supportSessionUuid, user);
    }

    //清除缓存一个用户信息
    public void expireCache(@NonNull String supportSessionUuid) {
        User user = this.cache.getIfPresent(supportSessionUuid);
        if (user != null) {
            this.cache.invalidate(supportSessionUuid);
            log.info("删除缓存用户 key={} nickname={} phone={}", supportSessionUuid, user.getNickname(), user.getPhone());
        } else {
            log.info("key={} 的用户已经不在缓存中了", supportSessionUuid);
        }
    }

    //更新一个缓存中的用户
    public void updateCacheUser(@NonNull User user) {
        ConcurrentMap<String, User> map = this.cache.asMap();
        map.forEach((key, userItem) -> {
            if (Objects.equals(userItem.getUuid(), user.getUuid())) {
                this.cacheUser(key, user);
                log.info("更新用户{}({})的guava缓存", user.getNickname(), user.getPhone());
            }
        });
    }

    //做权限拦截的事情。主要给AuthInterceptor使用。Hibernate Session必须通过@Transactional才可持久。
    @Transactional
    public boolean doAuthIntercept(HttpServletRequest request, HttpServletResponse response, Object handler) {

        HandlerMethod handlerMethod = null;
        if (handler instanceof ResourceHttpRequestHandler) {
            //资源文件处理器的拦截
            return true;
        } else {
            try {
                handlerMethod = (HandlerMethod) handler;
            } catch (Exception e) {
                throw new UtilException("转换HandlerMethod出错，请及时排查。");
            }
        }

        String authentication = NetworkUtil.getAuthentication(request);

        User user = this.getCachedUser(request);
        if (user == null) {

            //再尝试从数据库session中加载
            SupportSession supportSession = supportSessionService.find(authentication);

            if (supportSession != null) {

                //同时没有过期
                if (supportSession.getExpireTime().getTime() > System.currentTimeMillis()) {

                    user = this.find(supportSession.getUserUuid());

                    if (user != null) {

                        //将用户信息缓存起来
                        this.cacheUser(authentication, user);
                    }

                } else {
                    log.info("supportSession = {} 已经过期");
                }
            }

        }

        Feature feature = handlerMethod.getMethodAnnotation(Feature.class);

        if (feature != null) {
            FeatureType[] types = feature.value();

            //公共接口直接允许访问。
            if (types[0] == FeatureType.PUBLIC) {

                return true;

            } else {

                if (user == null) {
                    throw new LoginException();
                }

                //2.验证用户是否有权限访问该接口。
                StringBuilder typeString = new StringBuilder();
                for (FeatureType featureType : types) {

                    typeString.append(featureType).append(",");
                    if (user.hasPermission(featureType)) {
                        return true;
                    }

                }

                log.error("用户：" + user.getNickname() + " 角色：" + user.getRole() + " 功能点：" + typeString.toString() + " 接口：" + request.getRequestURI());
                throw new UnauthorizedException();

            }

        } else {
            throw new NotFoundException("您访问的接口不存在或者未配置Feature！");
        }


    }


    public Pager<User> page(

            Integer page,
            Integer pageSize,
            Sort.Direction orderSort,
            Sort.Direction orderUpdateTime,
            Sort.Direction orderCreateTime,

            Sort.Direction orderLastTime,
            String username,
            String email,
            String phone,
            String keyword,
            User.Role role,
            Boolean autoComplete
    ) {

        Sort sort = defaultSort(orderSort, orderUpdateTime, orderCreateTime);


        if (orderLastTime != null) {
            sort = this.and(sort, new Sort(orderLastTime, User_.lastTime.getName()));
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);


        Page<User> pageData = getDao().findAll(((root, query, cb) -> {
            Predicate predicate = cb.isNotNull(root.get(User_.uuid));

            if (username != null) {
                predicate = cb.and(predicate, cb.like(root.get(User_.username), "%" + username + "%"));
            }

            if (email != null) {
                predicate = cb.and(predicate, cb.like(root.get(User_.email), "%" + email + "%"));
            }

            if (phone != null) {
                predicate = cb.and(predicate, cb.like(root.get(User_.phone), "%" + phone + "%"));
            }

            if (role != null) {
                predicate = cb.and(predicate, cb.equal(root.get(User_.role), role));
            }

            if (keyword != null) {

                Predicate predicate1 = cb.like(root.get(User_.username), "%" + keyword + "%");
                Predicate predicate2 = cb.like(root.get(User_.email), "%" + keyword + "%");

                predicate = cb.and(predicate, cb.or(predicate1, predicate2));
            }
            return predicate;

        }), pageable);

        long totalItems = pageData.getTotalElements();
        List<User> list = pageData.getContent();

        return new Pager<>(page, pageSize, totalItems, list);


    }


}
