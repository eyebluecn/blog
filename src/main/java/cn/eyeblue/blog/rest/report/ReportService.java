package cn.eyeblue.blog.rest.report;

import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import javax.persistence.criteria.Predicate;
import java.util.List;

@Slf4j
@Service
public class ReportService extends BaseEntityService<Report> {

    @Autowired
    ReportDao reportDao;

    public ReportService() {
        super(Report.class);
    }


    public Pager<Report> page(
            Integer page,
            Integer pageSize,
            Sort.Direction orderSort,
            String entityUuid,
            String entityName,
            Report.Type type,
            String content,
            String ip
    ) {

        Sort sort = null;

        if (orderSort != null) {
            sort = new Sort(orderSort, Report_.sort.getName());
        }

        Pageable pageable = getPageRequest(page, pageSize, sort);

        Page<Report> pageData = getDao().findAll((root, query, cb) -> {
            Predicate predicate = cb.isNotNull(root.get(Report_.uuid));


            if (entityUuid != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Report_.entityUuid), entityUuid));
            }
            if (entityName != null) {
                predicate = cb.and(predicate, cb.like(root.get(Report_.entityName), "%" + entityName + "%"));
            }

            if (type != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Report_.type), type));
            }


            if (content != null) {
                predicate = cb.and(predicate, cb.like(root.get(Report_.content), "%" + content + "%"));
            }

            if (ip != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Report_.ip), ip));
            }


            return predicate;

        }, pageable);

        long totalItems = pageData.getTotalElements();
        List<Report> list = pageData.getContent();

        return new Pager<>(page, pageSize, totalItems, list);
    }


    @Transactional
    public void softDelete(String entityUuid) {
        int i = reportDao.deleteByEntityUuid(entityUuid);
    }

    //验证某个实体是否被举报过。
    @Transient
    public boolean isReported(String entityUuid) {
        int count = reportDao.countByEntityUuid(entityUuid);
        return count > 0;
    }
}
