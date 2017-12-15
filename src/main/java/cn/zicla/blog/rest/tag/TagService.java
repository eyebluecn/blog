package cn.zicla.blog.rest.tag;

import cn.zicla.blog.rest.article.Article;
import cn.zicla.blog.rest.base.Base;
import cn.zicla.blog.rest.base.BaseEntityService;
import cn.zicla.blog.rest.base.Pager;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Slf4j
@Service
public class TagService extends BaseEntityService<Tag> {

    @Autowired
    TagDao userDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    public TagService() {
        super(Tag.class);
    }


    public Pager<Tag> page(
            Integer page,
            Integer pageSize,
            Sort.Direction orderSort,
            String userUuid,
            String name
    ) {

        Sort sort = new Sort(Sort.Direction.ASC, Tag_.deleted.getName());

        if (orderSort != null) {
            sort = sort.and(new Sort(orderSort, Tag_.sort.getName()));
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);

        Page<Tag> pageData = getDao().findAll(((root, query, cb) -> {
            Predicate predicate = cb.equal(root.get(Tag_.deleted), false);

            if (userUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Tag_.userUuid), userUuid));
            }
            if (name != null) {
                predicate = cb.and(predicate, cb.like(root.get(Tag_.name), "%" + name + "%"));
            }

            return predicate;

        }), pageable);

        long totalItems = pageData.getTotalElements();
        List<Tag> list = pageData.getContent();

        return new Pager<>(page, pageSize, totalItems, list);
    }

}
