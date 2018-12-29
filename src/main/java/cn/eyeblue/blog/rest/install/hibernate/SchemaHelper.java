package cn.eyeblue.blog.rest.install.hibernate;

import lombok.Cleanup;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.internal.ExceptionHandlerHaltImpl;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToWriter;
import org.hibernate.tool.schema.spi.*;

import java.io.StringWriter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


@Data
@Slf4j
public class SchemaHelper {

    //直接返回需要执行的ddl语句
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static String getDdlSql(
            @NonNull EnumSet<TargetType> targetTypes,
            @NonNull Metadata metadata,
            @NonNull ServiceRegistry serviceRegistry) {

        @Cleanup StringWriter writer = new StringWriter();

        Map config = new HashMap(serviceRegistry.getService(ConfigurationService.class).getSettings());
        config.put(AvailableSettings.HBM2DDL_DELIMITER, ";");
        config.put(AvailableSettings.FORMAT_SQL, true);

        final SchemaManagementTool tool = serviceRegistry.getService(SchemaManagementTool.class);

        final ExceptionHandler exceptionHandler = ExceptionHandlerHaltImpl.INSTANCE;

        final ExecutionOptions executionOptions = SchemaManagementToolCoordinator.buildExecutionOptions(
                config,
                exceptionHandler
        );

        //将结果输出到一个Writer中去
        final TargetDescriptor targetDescriptor = new TargetDescriptor() {
            @Override
            public EnumSet<TargetType> getTargetTypes() {
                return targetTypes;
            }

            @Override
            public ScriptTargetOutput getScriptTargetOutput() {
                return new ScriptTargetOutputToWriter(writer);
            }
        };

        //获取迁移相关的元信息。
        SchemaMigrator schemaMigrator = tool.getSchemaMigrator(config);
        schemaMigrator.doMigration(metadata, executionOptions, targetDescriptor);

        return writer.toString();

    }

}

