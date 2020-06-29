package pl.exsio.querydsl.entityql.java.benchmark.execution.entityql;

import com.querydsl.sql.SQLQueryFactory;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;
import pl.exsio.querydsl.entityql.java.benchmark.EntityQLQueryExecutionBenchmark;
import pl.exsio.querydsl.entityql.java.dto.BookDto;
import pl.exsio.querydsl.entityql.java.jpa.entity.generated.QJBook;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
import static org.junit.Assert.assertTrue;

public class EntityQLGeneratedInstanceQueryExecutionBenchmark extends EntityQLQueryExecutionBenchmark {

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
        QJBook book = QJBook.INSTANCE;
        List<BookDto> books = settings.queryFactory
                .select(constructor(BookDto.class, book.id, book.name, book.desc, book.price))
                .from(book)
                .fetch();
        blackHole.consume(books);
    }

    @Test
    public void shouldRunEntityQLGeneratedQueryExecutionBenchmark() throws RunnerException {
        double score = runBenchmark();
        assertTrue(score >= 0);
    }
}
