package cn.zicla.blog.rest.article;

import cn.zicla.blog.rest.base.BaseEntityService;
import cn.zicla.blog.rest.base.Pager;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import cn.zicla.blog.rest.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Slf4j
@Service
public class ArticleService extends BaseEntityService<Article> {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    UserService userService;



    @Autowired
    SupportSessionDao supportSessionDao;

    public ArticleService() {
        super(Article.class);
    }

    public Pager<Article> page(

            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Sort.Direction orderSort,
            @RequestParam(required = false) Sort.Direction orderTop,
            @RequestParam(required = false) Sort.Direction orderHit,
            @RequestParam(required = false) Sort.Direction orderPrivacy,
            @RequestParam(required = false) Sort.Direction orderReleaseTime,
            @RequestParam(required = false) String userUuid,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String keyword
    ) {

        Sort sort = new Sort(Sort.Direction.ASC, Article_.deleted.getName());

        if (orderTop != null) {
            sort = sort.and(new Sort(orderTop, Article_.top.getName()));
        }

        if (orderSort != null) {
            sort = sort.and(new Sort(orderSort, Article_.sort.getName()));
        }



        if (orderHit != null) {
            sort = sort.and(new Sort(orderHit, Article_.hit.getName()));
        }

        if (orderPrivacy != null) {
            sort = sort.and(new Sort(orderPrivacy, Article_.privacy.getName()));
        }

        if (orderReleaseTime != null) {
            sort = sort.and(new Sort(orderReleaseTime, Article_.releaseTime.getName()));
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);

        Page<Article> pageData = getDao().findAll(((root, query, cb) -> {
            Predicate predicate = cb.equal(root.get(Article_.deleted), false);

            if (userUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Article_.userUuid), userUuid));
            }
            if (title != null) {
                predicate = cb.and(predicate, cb.like(root.get(Article_.title), "%" + title + "%"));
            }
            if (tag != null) {
                predicate = cb.and(predicate, cb.like(root.get(Article_.tags), "%" + tag + "%"));
            }

            if (keyword != null) {

                Predicate predicate1 = cb.like(root.get(Article_.title), "%" + keyword + "%");
                Predicate predicate2 = cb.like(root.get(Article_.html), "%" + keyword + "%");

                predicate = cb.and(predicate, cb.or(predicate1, predicate2));
            }
            return predicate;

        }), pageable);

        long totalItems = pageData.getTotalElements();
        List<Article> list = pageData.getContent();

        list.forEach(article -> article.setUser(userService.find(article.getUserUuid())));

        return new Pager<>(page, pageSize, totalItems, list);
    }

}
