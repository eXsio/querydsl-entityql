package pl.exsio.querydsl.entityql.config

import com.querydsl.sql.H2Templates
import com.querydsl.sql.SQLQueryFactory
import com.querydsl.sql.SQLTemplates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@ComponentScan("pl.exsio.querydsl.entityql")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class SpringContext {

    @Bean
    static DataSource dataSource() {
        new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    static LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        em.setPackagesToScan("pl.exsio.querydsl.entityql");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.hbm2ddl.import_files", "data.sql");
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    static PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    SQLTemplates sqlTemplates() {
        return new H2Templates(); //choose the implementation that matches your database engine
    }

    @Bean
    static SQLQueryFactory queryFactory(DataSource dataSource, SQLTemplates sqlTemplates) {
        return new EntityQlQueryFactory(new com.querydsl.sql.Configuration(sqlTemplates), dataSource)
        .registerEnumsByName("pl.exsio.querydsl.entityql.config.enums.by_name")
        .registerEnumsByOrdinal("pl.exsio.querydsl.entityql.config.enums.by_ordinal")
    }

}
