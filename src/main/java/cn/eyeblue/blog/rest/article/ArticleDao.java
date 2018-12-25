package cn.eyeblue.blog.rest.article;

import cn.eyeblue.blog.rest.base.BaseEntityDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao extends BaseEntityDao<Article> {


    //统计某人的文章各种参数总和。+0是奇技淫巧，把字符串转成数字。
    @Query(value = "select sum(a.agree) as agree,sum(a.words) as words,sum(a.hit) as hit,sum(a.commentNum) as commentNum from Article a where a.privacy=false and a.userUuid=:userUuid")
    Object analysisTotal(@Param("userUuid") String userUuid);

    int countByUserUuidAndPrivacyFalse(String userUuid);

    int countByUserUuidAndPath(String userUuid, String path);

    //某个用户下面的文章路径查重
    int countByUserUuidAndTypeAndPath(String userUuid, ArticleType type, String path);

    //文档下的路径要求唯一
    int countByUserUuidAndDocumentUuidAndPath(String userUuid, String documentUuid, String path);

    Article findByUserUuidAndTypeAndPath(String userUuid, ArticleType type, String path);

    //根据path找出某个文档下的某篇文章
    Article findByUserUuidAndDocumentUuidAndPath(String userUuid, String documentUuid, String path);


    //找出某个puuid下面所有的子集
    List<Article> findByDocumentUuidAndPuuid(String documentUuid, String puuid);
}
