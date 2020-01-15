package pl.exsio.querydsl.entityql.benchmark.building;

import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;
import pl.exsio.querydsl.entityql.benchmark.EntityQLQueryExecutionBenchmark;
import pl.exsio.querydsl.entityql.dto.BookDto;
import pl.exsio.querydsl.entityql.jpa.entity.generated.QJBook;

import static com.querydsl.core.types.Projections.constructor;
import static org.junit.Assert.assertTrue;

public class EntityQLGeneratedConstructorQueryBuildingBenchmark extends EntityQLQueryExecutionBenchmark {


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
        QJBook book = new QJBook();
        SQLQuery<BookDto> query = settings.queryFactory
                .select(constructor(BookDto.class,
                        book.id,
                        book.name,
                        book.desc,
                        book.price
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
