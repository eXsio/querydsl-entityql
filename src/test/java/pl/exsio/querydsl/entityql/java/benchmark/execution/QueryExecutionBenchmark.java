package pl.exsio.querydsl.entityql.java.benchmark.execution;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import pl.exsio.querydsl.entityql.java.benchmark.PerformanceBenchmark;

import javax.sql.DataSource;

public abstract class QueryExecutionBenchmark implements PerformanceBenchmark {

    public static DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("performance-test-schema.sql")
                .addScript("performance-test-data.sql")
                .build();
    }
}
