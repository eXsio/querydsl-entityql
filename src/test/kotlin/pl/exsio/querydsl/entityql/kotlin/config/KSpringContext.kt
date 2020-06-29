package pl.exsio.querydsl.entityql.kotlin.config

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
import pl.exsio.querydsl.entityql.config.EntityQlQueryFactory
import java.util.*

import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@ComponentScan("pl.exsio.querydsl.entityql.kotlin")
@EnableAspectJAutoProxy
@EnableTransactionManagement
open class KSpringContext {

    @Bean
    open fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).setName(UUID.randomUUID().toString()).build();
    }

    @Bean
    open fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("pl.exsio.querydsl.entityql.kotlin");
        em.setJpaVendorAdapter(HibernateJpaVendorAdapter());

        val properties = Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.hbm2ddl.import_files", "data.sql");
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    open fun transactionManager(emf: EntityManagerFactory): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    open fun sqlTemplates(): SQLTemplates {
        return H2Templates(); //choose the implementation that matches your database engine
    }

    @Bean
    open fun queryFactory(dataSource: DataSource, sqlTemplates: SQLTemplates): SQLQueryFactory {
        return EntityQlQueryFactory(com.querydsl.sql.Configuration(sqlTemplates), dataSource)
                .registerEnumsByName("pl.exsio.querydsl.entityql.kotlin.config.enums.by_name")
                .registerEnumsByOrdinal("pl.exsio.querydsl.entityql.kotlin.config.enums.by_ordinal")
    }

}
