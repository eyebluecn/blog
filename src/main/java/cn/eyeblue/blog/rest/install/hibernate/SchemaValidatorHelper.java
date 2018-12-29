package cn.eyeblue.blog.rest.install.hibernate;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Selectable;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.extract.spi.ColumnInformation;
import org.hibernate.tool.schema.extract.spi.TableInformation;
import org.hibernate.tool.schema.internal.ExceptionHandlerHaltImpl;
import org.hibernate.tool.schema.spi.*;
import org.hibernate.type.descriptor.JdbcTypeNameMapper;

import java.util.*;

@Slf4j
public class SchemaValidatorHelper {


    //验证数据库表的正确性。
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static void validate(
            @NonNull EnumSet<TargetType> targetTypes,
            @NonNull Metadata metadata,
            @NonNull ServiceRegistry serviceRegistry) {

        Map config = new HashMap(serviceRegistry.getService(ConfigurationService.class).getSettings());
        config.put(AvailableSettings.FORMAT_SQL, true);
        config.put(AvailableSettings.HBM2DDL_DELIMITER, ";");

        final SchemaManagementTool tool = serviceRegistry.getService(SchemaManagementTool.class);

        final ExceptionHandler exceptionHandler = ExceptionHandlerHaltImpl.INSTANCE;

        final ExecutionOptions executionOptions = SchemaManagementToolCoordinator.buildExecutionOptions(
                config,
                exceptionHandler
        );

        //这里去验证表的完整性
        SchemaValidator schemaValidator = tool.getSchemaValidator(config);
        schemaValidator.doValidation(metadata, executionOptions);


    }

    protected void validateColumnType(
            Table table,
            Column column,
            ColumnInformation columnInformation,
            Metadata metadata,
            ExecutionOptions options,
            Dialect dialect) {
        boolean typesMatch = column.getSqlTypeCode(metadata) == columnInformation.getTypeCode()
                || column.getSqlType(dialect, metadata).toLowerCase(Locale.ROOT).startsWith(columnInformation.getTypeName().toLowerCase(Locale.ROOT));
        if (!typesMatch) {
            throw new SchemaManagementException(
                    String.format(
                            "Schema-validation: wrong column type encountered in column [%s] in " +
                                    "table [%s]; found [%s (Types#%s)], but expecting [%s (Types#%s)]",
                            column.getName(),
                            table.getQualifiedTableName(),
                            columnInformation.getTypeName().toLowerCase(Locale.ROOT),
                            JdbcTypeNameMapper.getTypeName(columnInformation.getTypeCode()),
                            column.getSqlType().toLowerCase(Locale.ROOT),
                            JdbcTypeNameMapper.getTypeName(column.getSqlTypeCode(metadata))
                    )
            );
        }

        // this is the old Hibernate check...
        //
        // but I think a better check involves checks against type code and then the type code family, not
        // just the type name.
        //
        // See org.hibernate.type.descriptor.sql.JdbcTypeFamilyInformation
        // todo : this ^^
    }
//
//    public void validateDatabase(Metadata metadata, ExecutionOptions options, HibernateSchemaManagementTool tool) {
//
//        final JdbcContext jdbcContext = tool.resolveJdbcContext(options.getConfigurationValues());
//
//        final DdlTransactionIsolator isolator = tool.getDdlTransactionIsolator(jdbcContext);
//
//        final DatabaseInformation databaseInformation = Helper.buildDatabaseInformation(
//                tool.getServiceRegistry(),
//                isolator,
//                metadata.getDatabase().getDefaultNamespace().getName()
//        );
//
//        try {
//            performValidation(metadata, databaseInformation, options, jdbcContext.getDialect());
//        } finally {
//            try {
//                databaseInformation.cleanup();
//            } catch (Exception e) {
//                log.debug("Problem releasing DatabaseInformation : " + e.getMessage());
//            }
//
//            isolator.release();
//        }
//    }


//
//    public void performValidation(
//            Metadata metadata,
//            DatabaseInformation databaseInformation,
//            ExecutionOptions options,
//            Dialect dialect) {
//        for ( Namespace namespace : metadata.getDatabase().getNamespaces() ) {
//            if ( schemaFilter.includeNamespace( namespace ) ) {
//                validateTables( metadata, databaseInformation, options, dialect, namespace );
//            }
//        }
//
//        for ( Namespace namespace : metadata.getDatabase().getNamespaces() ) {
//            if ( schemaFilter.includeNamespace( namespace ) ) {
//                for ( Sequence sequence : namespace.getSequences() ) {
//                    if ( schemaFilter.includeSequence( sequence ) ) {
//                        final SequenceInformation sequenceInformation = databaseInformation.getSequenceInformation(
//                                sequence.getName()
//                        );
//                        validateSequence( sequence, sequenceInformation );
//                    }
//                }
//            }
//        }
//    }
//

    //验证数据库表是否正确
    protected void validateTable(
            Table table,
            TableInformation tableInformation,
            Metadata metadata,
            ExecutionOptions options,
            Dialect dialect) {


        if (tableInformation == null) {
            throw new SchemaManagementException(
                    String.format(
                            "Schema-validation: missing table [%s]",
                            table.getQualifiedTableName().toString()
                    )
            );
        }

        final Iterator selectableItr = table.getColumnIterator();
        while (selectableItr.hasNext()) {
            final Selectable selectable = (Selectable) selectableItr.next();
            if (Column.class.isInstance(selectable)) {
                final Column column = (Column) selectable;
                final ColumnInformation existingColumn = tableInformation.getColumn(Identifier.toIdentifier(column.getQuotedName()));
                if (existingColumn == null) {
                    throw new SchemaManagementException(
                            String.format(
                                    "Schema-validation: missing column [%s] in table [%s]",
                                    column.getName(),
                                    table.getQualifiedTableName()
                            )
                    );
                }
                validateColumnType(table, column, existingColumn, metadata, options, dialect);
            }
        }
    }


}
