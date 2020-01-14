package pl.exsio.querydsl.entityql.config;

import com.querydsl.sql.*;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.querydsl.sql.types.BooleanType;
import com.querydsl.sql.types.UtilUUIDType;
import org.reflections.Reflections;
import org.springframework.jdbc.datasource.DataSourceUtils;
import pl.exsio.querydsl.entityql.type.QEnumType;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EntityQL's extension of QueryDSL SQLQueryFactory.
 *
 * Designed to work seamlessly with Spring's Transaction Management.
 * If the Connection obtained from Spring is transactional, QueryDSL will not close it.
 * If the Connection is not transactional, QueryDSL will automatically close it.
 *
 * Automatically registers all Java Enums for given packages with the Query Factory.
 */
public class EntityQlQueryFactory extends SQLQueryFactory {

    public EntityQlQueryFactory(Configuration configuration, DataSource dataSource, String... enumPackages) {
        super(configuration, () -> DataSourceUtils.getConnection(dataSource));
        registerCustomTypes(configuration, enumPackages);
        closeConnectionIfNoTransaction(configuration, dataSource);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
    }

    private void closeConnectionIfNoTransaction(Configuration configuration, DataSource dataSource) {
        configuration.addListener(new SQLBaseListener() {

            @Override
            public void end(SQLListenerContext context) {
                if (!DataSourceUtils.isConnectionTransactional(context.getConnection(), dataSource)) {
                    SQLCloseListener.DEFAULT.end(context);
                }
            }
        });
    }

    private void registerCustomTypes(Configuration configuration, String[] enumPackages) {
        Arrays.stream(enumPackages).forEach(enumPackage ->
                getEnumTypes(enumPackage).forEach(configuration::register)
        );
        configuration.register(new UtilUUIDType());
        configuration.register(new BooleanType(Types.NUMERIC));
    }

    private List<QEnumType<?>> getEnumTypes(String enumPackage) {
        return new Reflections(enumPackage)
                .getSubTypesOf(Enum.class).stream()
                .map(this::createEnumType)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private QEnumType<?> createEnumType(Class<? extends Enum> enumClass) {
        return new QEnumType<>(enumClass);
    }
}
