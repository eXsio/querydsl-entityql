package pl.exsio.querydsl.entityql.benchmark;

import org.junit.Test;
import org.openjdk.jmh.runner.RunnerException;

import static org.junit.Assert.assertTrue;

public class GeneratedConstructorPerformanceBenchmark implements PerformanceBenchmark {

//    @Benchmark
//    @Fork(value = 1)
//    @Warmup(iterations = 6, time = 10)
//    @Measurement(iterations = 6, time = 10)
//    @BenchmarkMode(Mode.Throughput)
//    public void run(Blackhole blackHole) {
//        blackHole.consume(new QOrderItem());
//    }

    @Test
    public void shouldRunGeneratedMetaModelCreationBenchmark() throws RunnerException {
        double score = runBenchmark();
        assertTrue(score >= 0);
    }
}