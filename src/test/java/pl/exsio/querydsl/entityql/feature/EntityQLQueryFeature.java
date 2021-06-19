package pl.exsio.querydsl.entityql.feature;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import pl.exsio.querydsl.entityql.benchmark.PerformanceBenchmark;
import pl.exsio.querydsl.entityql.benchmark.execution.QueryExecutionBenchmark;
import pl.exsio.querydsl.entityql.config.EntityQlQueryFactory;

import javax.sql.DataSource;

public abstract class EntityQLQueryFeature {

    public static DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("feature-test-schema.sql")
                .build();
    }

    public static SQLQueryFactory queryFactory() {
        return new EntityQlQueryFactory(new Configuration(new H2Templates()), dataSource());
    }
}
