package pl.exsio.querydsl.entityql.benchmark;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public interface PerformanceBenchmark {

    default double runBenchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(getClass().getSimpleName())
                .build();
        List<RunResult> runResults = new ArrayList<>(new Runner(opt).run());
        assert !runResults.isEmpty();
        return runResults.get(0).getPrimaryResult().getScore();
    }


}
