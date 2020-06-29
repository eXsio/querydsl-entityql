package pl.exsio.querydsl.entityql.java.benchmark.execution.jpa;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import pl.exsio.querydsl.entityql.java.benchmark.PerformanceBenchmark;
import pl.exsio.querydsl.entityql.java.benchmark.execution.QueryExecutionBenchmark;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

abstract class JpaQueryExecutionBenchmark extends QueryExecutionBenchmark implements PerformanceBenchmark {

    static EntityManagerFactory emf() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("pl.exsio.querydsl.entityql.jpa.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(new Properties());
        em.afterPropertiesSet();
        return em.getObject();
    }
}
