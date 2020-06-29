package pl.exsio.querydsl.entityql.java.benchmark.obtaining;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;
import pl.exsio.querydsl.entityql.java.benchmark.PerformanceBenchmark;
import pl.exsio.querydsl.entityql.java.jpa.entity.generated.QJBook;

import static org.junit.Assert.assertTrue;

public class GeneratedInstancePerformanceBenchmark implements PerformanceBenchmark {

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 6, time = 10)
    @Measurement(iterations = 6, time = 10)
    @BenchmarkMode(Mode.Throughput)
    public void run(Blackhole blackHole) {
        blackHole.consume(QJBook.INSTANCE);
    }

    @Test
    public void shouldRunGeneratedMetaModelCreationBenchmark() throws RunnerException {
        double score = runBenchmark();
        assertTrue(score >= 0);
    }
}
