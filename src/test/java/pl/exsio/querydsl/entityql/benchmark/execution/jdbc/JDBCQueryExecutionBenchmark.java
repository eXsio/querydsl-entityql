package pl.exsio.querydsl.entityql.benchmark.execution.jdbc;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLQueryFactory;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import pl.exsio.querydsl.entityql.benchmark.PerformanceBenchmark;
import pl.exsio.querydsl.entityql.benchmark.execution.QueryExecutionBenchmark;
import pl.exsio.querydsl.entityql.config.EntityQlQueryFactory;
import pl.exsio.querydsl.entityql.config.entity.generated.QJBook;
import pl.exsio.querydsl.entityql.dto.BookDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static org.junit.Assert.assertTrue;

public class JDBCQueryExecutionBenchmark extends QueryExecutionBenchmark {


    @State(Scope.Benchmark)
    public static class Settings {

        DataSource dataSource;

        @Setup
        public void setup() {
            dataSource = dataSource();
        }

    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 6, time = 10)
    @Measurement(iterations = 6, time = 10)
    @BenchmarkMode(Mode.Throughput)
    public void run(Blackhole blackHole, Settings settings) throws Exception {
        Connection c = settings.dataSource.getConnection();
        ResultSet rs = c.createStatement().executeQuery("select \"BOOK_ID\", \"DESC\", \"NAME\", \"PRICE\" from \"JBOOKS\"");
        List<BookDto> books = new ArrayList<>();
        while (rs.next()) {
            books.add(new BookDto(rs.getLong(1),rs.getString(2),rs.getString(3),rs.getBigDecimal(4)));
        }
        rs.close();
        c.close();
        blackHole.consume(books);
    }

    @Test
    public void shouldRunJdbcQueryExecutionBenchmark() throws RunnerException {
        double score = runBenchmark();
        assertTrue(score >= 0);
    }
}
