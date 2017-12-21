package cn.eyeblue.blog.config;

import cn.eyeblue.blog.rest.base.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

/**
 * 数据库表以及字段的命名规则
 */
@Slf4j
public class NamingStrategyConfiguration extends SpringPhysicalNamingStrategy {


    @Override
    public Identifier toPhysicalCatalogName(Identifier name,
                                            JdbcEnvironment jdbcEnvironment) {


        return super.toPhysicalCatalogName(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name,
                                           JdbcEnvironment jdbcEnvironment) {


        return super.toPhysicalSchemaName(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name,
                                          JdbcEnvironment jdbcEnvironment) {


        Identifier identifier = super.toPhysicalTableName(name, jdbcEnvironment);
        String rawTableName = identifier.getText();

        String tableName = BaseEntity.PREFIX + rawTableName;

        //测试中的表都是以Fake开头的。
        if (rawTableName.startsWith("fake_")) {
            tableName = BaseEntity.PREFIX + rawTableName.substring("fake_".length());
        }


        return new Identifier(tableName, identifier.isQuoted());
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name,
                                             JdbcEnvironment jdbcEnvironment) {


        return super.toPhysicalSequenceName(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name,
                                           JdbcEnvironment jdbcEnvironment) {


        return super.toPhysicalColumnName(name, jdbcEnvironment);
    }

}
