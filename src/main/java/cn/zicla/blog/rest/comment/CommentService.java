package cn.zicla.blog.rest.comment;

import cn.zicla.blog.rest.base.Base;
import cn.zicla.blog.rest.base.BaseEntityService;
import cn.zicla.blog.rest.base.Pager;
import cn.zicla.blog.rest.base.WebResult;
import cn.zicla.blog.rest.core.Feature;
import cn.zicla.blog.rest.core.FeatureType;
import cn.zicla.blog.rest.support.session.SupportSessionDao;
import cn.zicla.blog.util.JsonUtil;
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
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService extends BaseEntityService<Comment> {

    @Autowired
    CommentDao userDao;

    @Autowired
    SupportSessionDao supportSessionDao;

    public CommentService() {
        super(Comment.class);
    }

    public Pager<Comment> page(
            Integer page,
            Integer pageSize,
            Sort.Direction orderSort,
            String userUuid,
            String articleUuid,
            Boolean isFloor,
            String floorUuid,
            String puuid,
            String name,
            String email,
            String content,
            Boolean isReport,
            String report
    ) {

        Sort sort = new Sort(Sort.Direction.ASC, Comment_.deleted.getName());

        if (orderSort != null) {
            sort = sort.and(new Sort(orderSort, Comment_.sort.getName()));
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);

        Page<Comment> pageData = getDao().findAll((root, query, cb) -> {
            Predicate predicate = cb.equal(root.get(Comment_.deleted), false);

            if (articleUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.articleUuid), articleUuid));
            }
            if (userUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.userUuid), userUuid));
            }
            if (isFloor != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.isFloor), isFloor));
            }
            if (floorUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.floorUuid), floorUuid));
            }
            if (puuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.puuid), puuid));
            }

            if (name != null) {
                predicate = cb.and(predicate, cb.like(root.get(Comment_.name), "%" + name + "%"));
            }

            if (email != null) {
                predicate = cb.and(predicate, cb.like(root.get(Comment_.email), "%" + email + "%"));
            }
            if (content != null) {
                predicate = cb.and(predicate, cb.like(root.get(Comment_.content), "%" + content + "%"));
            }
            if (isReport != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.isReport), isReport));
            }
            if (report != null) {
                predicate = cb.and(predicate, cb.like(root.get(Comment_.report), "%" + report + "%"));
            }
            return predicate;

        }, pageable);

        long totalItems = pageData.getTotalElements();
        List<Comment> list = pageData.getContent();

        return new Pager<>(page, pageSize, totalItems, list);
    }

}
