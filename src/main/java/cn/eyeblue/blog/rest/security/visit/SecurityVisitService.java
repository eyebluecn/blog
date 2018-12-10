package cn.eyeblue.blog.rest.security.visit;

import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SecurityVisitService extends BaseEntityService<SecurityVisit> {

    @Autowired
    SecurityVisitDao securityVisitDao;


    public SecurityVisitService() {
        super(SecurityVisit.class);
    }


    public Pager<SecurityVisit> page(
            Integer page,
            Integer pageSize,
            Sort.Direction orderSort,
            Sort.Direction orderUpdateTime,
            Sort.Direction orderCreateTime,
            Date startTime,
            Date endTime,
            String userUuid
    ) {

        Sort sort = defaultSort(orderSort, orderUpdateTime, orderCreateTime);

        Pageable pageable = getPageRequest(page, pageSize, sort);

        Page<SecurityVisit> pageData = getDao().findAll((root, query, cb) -> {

            Predicate predicate = cb.isNotNull(root.get(SecurityVisit_.uuid));


            if (userUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(SecurityVisit_.userUuid), userUuid));
            }


            if (startTime != null && endTime != null) {
                predicate = cb.and(predicate, cb.between(root.get(SecurityVisit_.createTime), startTime, endTime));
            }


            return predicate;

        }, pageable);

        long totalItems = pageData.getTotalElements();
        List<SecurityVisit> list = pageData.getContent();

        return new Pager<>(pageable.getPageNumber(), pageable.getPageSize(), totalItems, list);
    }

    @Transactional
    public void del(String uuid) {
        SecurityVisit securityVisit = this.check(uuid);

        securityVisitDao.delete(securityVisit);


    }


}
