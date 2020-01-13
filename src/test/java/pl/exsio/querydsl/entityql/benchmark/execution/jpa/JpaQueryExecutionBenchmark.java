package pl.exsio.querydsl.entityql.benchmark.execution.jpa;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import pl.exsio.querydsl.entityql.benchmark.PerformanceBenchmark;
import pl.exsio.querydsl.entityql.benchmark.execution.QueryExecutionBenchmark;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

abstract class JpaQueryExecutionBenchmark extends QueryExecutionBenchmark implements PerformanceBenchmark {

    static EntityManagerFactory emf() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("pl.exsio.querydsl.entityql.config.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(new Properties());
        em.afterPropertiesSet();
        return em.getObject();
    }
}
