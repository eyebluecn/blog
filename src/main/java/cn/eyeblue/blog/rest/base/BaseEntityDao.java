package cn.eyeblue.blog.rest.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;


@NoRepositoryBean
public interface BaseEntityDao<T extends BaseEntity> extends CrudRepository<T, String> {

    /**
     * 按照分页的方式获取列表
     *
     * @param pageable 一个分页对象
     * @return 返回分页的列表
     */
    Page<T> findAll(Pageable pageable);

    /**
     * 提供一个复杂查询
     *
     * @param spec     复杂
     * @param pageable 分页
     * @return 有条件的分页结果
     */
    Page<T> findAll(Specification<T> spec, Pageable pageable);


    /**
     * 提供一个复杂查询
     *
     * @param spec 复杂查询
     * @return List结果
     */
    List<T> findAll(Specification<T> spec);

    /**
     * 按照给定的条件来计数
     *
     * @param spec 复杂
     * @return 满足条件的数量
     */
    Integer count(Specification<T> spec);
}
