package pl.exsio.querydsl.entityql.config;

import com.querydsl.sql.*;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.querydsl.sql.types.*;
import org.reflections.Reflections;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * EntityQL's extension of QueryDSL SQLQueryFactory.
 * <p>
 * Designed to work seamlessly with Spring's Transaction Management.
 * If the Connection obtained from Spring is transactional, QueryDSL will not close it.
 * If the Connection is not transactional, QueryDSL will automatically close it.
 * <p>
 * Automatically registers all Java Enums for given packages with the Query Factory.
 */
public class EntityQlQueryFactory extends SQLQueryFactory {

    public EntityQlQueryFactory(Configuration configuration, DataSource dataSource) {
        super(configuration, () -> DataSourceUtils.getConnection(dataSource));
        registerCustomTypes(configuration);
        closeConnectionIfNoTransaction(configuration, dataSource);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
    }

    public EntityQlQueryFactory registerEnumsByName(String enumPackage) {
        List<AbstractType<?>> enumTypes = getEnumTypes(enumPackage, EnumByNameType::new);
        enumTypes.forEach(configuration::register);
        return this;
    }

    public EntityQlQueryFactory registerEnumsByOrdinal(String enumPackage) {
        List<AbstractType<?>> enumTypes = getEnumTypes(enumPackage, EnumByOrdinalType::new);
        enumTypes.forEach(configuration::register);
        return this;
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

    private void registerCustomTypes(Configuration configuration) {
        configuration.register(new UtilUUIDType());
        configuration.register(new BooleanType(Types.NUMERIC));
    }

    private List<AbstractType<?>> getEnumTypes(String enumPackage,
                                               Function<Class<? extends Enum>, AbstractType<?>> enumTypeCreator
    ) {
        return new Reflections(enumPackage)
                .getSubTypesOf(Enum.class).stream()
                .map(enumTypeCreator::apply)
                .collect(Collectors.toList());
    }

}
