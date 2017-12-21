package cn.zicla.blog.rest.tag;

import cn.zicla.blog.config.exception.UtilException;
import cn.zicla.blog.rest.article.Article;
import cn.zicla.blog.rest.base.Base;
import cn.zicla.blog.rest.base.BaseEntityService;
import cn.zicla.blog.rest.base.Pager;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import cn.zicla.blog.rest.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TagService extends BaseEntityService<Tag> {

    @Autowired
    TagDao tagDao;


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

    public List<Tag> checkTags(List<String> tagUuids, User operator) {
        List<Tag> tags = getTagsByUuids(tagUuids);
        for (Tag tag : tags) {
            if (!operator.getUuid().equals(tag.getUserUuid())) {
                throw new UtilException("标签【" + tag.getName() + "】不属于您！");
            }
        }
        return tags;
    }

    //根据Uuids获取tag数组。sql使用的是in，不存在的uuid会自动去除。
    public List<Tag> getTagsByUuids(List<String> uuids) {
        Iterable<Tag> all = tagDao.findAll(uuids);
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : all) {
            tags.add(tag);
        }
        return tags;
    }


}
