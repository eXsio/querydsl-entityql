package pl.exsio.querydsl.entityql.java.benchmark.building;

import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;
import pl.exsio.querydsl.entityql.EntityQL;
import pl.exsio.querydsl.entityql.Q;
import pl.exsio.querydsl.entityql.java.benchmark.EntityQLQueryExecutionBenchmark;
import pl.exsio.querydsl.entityql.java.dto.BookDto;
import pl.exsio.querydsl.entityql.java.jpa.entity.JBook;

import static com.querydsl.core.types.Projections.constructor;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EntityQLDynamicQueryBuildingBenchmark extends EntityQLQueryExecutionBenchmark {


    @State(Scope.Benchmark)
    public static class Settings {

        SQLQueryFactory queryFactory;

        @Setup
        public void setup() {
            queryFactory = queryFactory();
        }

    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 6, time = 10)
    @Measurement(iterations = 6, time = 10)
    @BenchmarkMode(Mode.Throughput)
    public void run(Blackhole blackHole, Settings settings) {
        Q<JBook> book = EntityQL.qEntity(JBook.class);
        SQLQuery<BookDto> query = settings.queryFactory
                .select(constructor(BookDto.class,
                        book.longNumber("id"),
                        book.string("name"),
                        book.string("desc"),
                        book.decimalNumber("price")
                ))
                .from(book);
        blackHole.consume(query);
    }

    @Test
    public void shouldRunEntityQLDynamicQueryExecutionBenchmark() throws RunnerException {
        double score = runBenchmark();
        assertTrue(score >= 0);
    }
}
