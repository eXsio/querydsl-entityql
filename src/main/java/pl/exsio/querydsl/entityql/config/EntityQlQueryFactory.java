package pl.exsio.querydsl.entityql.config;

import com.querydsl.sql.*;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.querydsl.sql.types.BooleanType;
import com.querydsl.sql.types.UtilUUIDType;
import org.reflections.Reflections;
import org.springframework.jdbc.datasource.DataSourceUtils;
import pl.exsio.querydsl.entityql.type.EnumType;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<EnumType<?>> getEnumTypes(String enumPackage) {
        return new Reflections(enumPackage)
                .getSubTypesOf(Enum.class).stream()
                .map(this::createEnumType)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private EnumType<?> createEnumType(Class<? extends Enum> enumClass) {
        return new EnumType<>(enumClass);
    }
}
