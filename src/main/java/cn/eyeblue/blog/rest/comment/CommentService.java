package cn.eyeblue.blog.rest.comment;

import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.support.session.SupportSessionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

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
            Sort.Direction orderUpdateTime,
            Sort.Direction orderCreateTime,

            String uuid,
            String userUuid,
            String articleUuid,
            Boolean isFloor,
            String floorUuid,
            String puuid,
            String name,
            String email,
            String content
    ) {

        Sort sort = defaultSort(orderSort, orderUpdateTime, orderCreateTime);


        Pageable pageable = getPageRequest(page, pageSize, sort);

        Page<Comment> pageData = getDao().findAll((root, query, cb) -> {
            Predicate predicate = cb.isNotNull(root.get(Comment_.uuid));

            if (uuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Comment_.uuid), uuid));
            }

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

            return predicate;

        }, pageable);

        long totalItems = pageData.getTotalElements();
        List<Comment> list = pageData.getContent();

        return new Pager<>(page, pageSize, totalItems, list);
    }

}
