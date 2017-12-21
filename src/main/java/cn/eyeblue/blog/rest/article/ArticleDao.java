package cn.eyeblue.blog.rest.article;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Repository
public interface ArticleDao extends BaseEntityDao<Article> {


    //统计某人的文章各种参数总和。+0是奇技淫巧，把字符串转成数字。
    @Query(value = "select sum(a.agree) as agree,sum(a.words) as words,sum(a.hit) as hit,sum(a.commentNum) as commentNum from Article a where a.privacy=false and a.userUuid=:userUuid and a.deleted = false")
    Object analysisTotal(@Param("userUuid") String userUuid);

    int countByUserUuidAndPrivacyFalseAndDeletedFalse(String userUuid);

}
