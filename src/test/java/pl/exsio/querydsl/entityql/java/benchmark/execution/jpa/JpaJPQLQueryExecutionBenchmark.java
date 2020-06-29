package pl.exsio.querydsl.entityql.java.benchmark.execution.jpa;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;
import pl.exsio.querydsl.entityql.java.dto.BookDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class JpaJPQLQueryExecutionBenchmark extends JpaQueryExecutionBenchmark {

    @State(Scope.Benchmark)
    public static class Settings {

        EntityManagerFactory emf;

        @Setup
        public void setup() {
            emf = emf();
        }
    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 6, time = 10)
    @Measurement(iterations = 6, time = 10)
    @BenchmarkMode(Mode.Throughput)
    public void run(Blackhole blackHole, Settings settings) {
        EntityManager em = settings.emf.createEntityManager();
        List<BookDto> books = em.createQuery(
                "select new pl.exsio.querydsl.entityql.dto.BookDto(b.id, b.name, b.desc, b.price) from JBook b", BookDto.class
        ).getResultList();
        blackHole.consume(books);
        em.close();
    }

    @Test
    public void shouldRunJpaJPQLQueryExecutionBenchmark() throws RunnerException {
        double score = runBenchmark();
        assertTrue(score >= 0);
    }
}
