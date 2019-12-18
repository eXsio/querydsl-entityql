package pl.exsio.querydsl.entityql.spec

import com.querydsl.core.types.dsl.DatePath
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.ForeignKey
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.config.entity.spec.NoIdSpec
import pl.exsio.querydsl.entityql.config.entity.spec.Spec
import pl.exsio.querydsl.entityql.config.entity.spec.SubSpec
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException
import pl.exsio.querydsl.entityql.ex.MissingIdException
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

import static pl.exsio.querydsl.entityql.EntityQL.qEntity

class QSpec extends Specification {


    def "should correctly get existing column"() {
        when:
        Q<Spec> unit = qEntity(Spec)

        then:
        unit.longNumber("id") instanceof NumberPath<Long>
        unit.string("string") instanceof StringPath
        unit.decimalNumber("decimalNumber") instanceof NumberPath<BigDecimal>
        unit.shortNumber("shortNumber") instanceof NumberPath<Short>
        unit.date("date") instanceof DatePath<LocalDate>
        unit.dateTime("dateTime") instanceof DateTimePath<LocalDateTime>
        unit.<Integer> number("intNumber") instanceof NumberPath<Integer>
        unit.<LocalDate> column("date") instanceof DatePath<LocalDate>
        unit.<LocalDate> comparableColumn("date") instanceof DatePath<LocalDate>
    }

    def "should correctly throw exception when non existent column is fetched"() {
        given:
        Q<Spec<String>> unit = qEntity(Spec)
        when:
        unit.longNumber("nonExistentColumn")

        then:
        thrown InvalidArgumentException
    }

    def "should correctly get existing Foreign Keys"() {
        when:
        Q<SubSpec> subUnit = qEntity(SubSpec)

        then:
        subUnit.<Spec<String>> joinColumn("unit") instanceof ForeignKey<Spec>
    }

    def "should correctly throw exception when non existent Foreign Key is fetched"() {
        given:
        Q<SubSpec> subUnit = qEntity(SubSpec)

        when:
        subUnit.<Spec<String>> joinColumn("nonExistentFk")

        then:
        thrown InvalidArgumentException
    }

    def "should correctly get Primary Key"() {
        when:
        Q<SubSpec> subUnit = qEntity(SubSpec)

        then:
        subUnit.primaryKey() instanceof PrimaryKey<SubSpec>
    }

    def "should correctly throw exception on invalid Primary Key"() {
        when:
        qEntity(NoIdSpec)

        then:
        thrown MissingIdException
    }

    def "should correctly create new instances of QType each time get() is called"() {
        expect:
        t1.equals(t2) == outcome

        where:
        t1                    | t2                    | outcome
        qEntity(Spec)         | qEntity(Spec)         | true
        qEntity(Spec)         | qEntity(SubSpec)      | false
        qEntity(Spec)         | qEntity(Spec, "var1") | false
        qEntity(Spec, "var1") | qEntity(Spec, "var2") | false
    }
}
