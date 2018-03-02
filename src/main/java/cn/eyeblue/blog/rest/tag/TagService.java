package cn.eyeblue.blog.rest.tag;

import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    //验证tag的uuid和创建者是否匹配
    public List<Tag> checkTags(List<String> tagUuids, String creatorUuid) {
        List<Tag> tags = getTagsByUuids(tagUuids);

        for (Tag tag : tags) {
            if (!creatorUuid.equals(tag.getUserUuid())) {
                throw new UtilException("标签和文章创建者不匹配！");
            }
        }
        return tags;
    }

    //根据Uuids获取tag数组。sql使用的是in，不存在的uuid会自动去除。
    public List<Tag> getTagsByUuids(List<String> uuids) {
        Iterable<Tag> all = tagDao.findAllById(uuids);
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : all) {
            tags.add(tag);
        }
        return tags;
    }


}
