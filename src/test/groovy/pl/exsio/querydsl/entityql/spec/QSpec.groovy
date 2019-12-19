package pl.exsio.querydsl.entityql.spec

import com.querydsl.core.types.dsl.DatePath
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.ForeignKey
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.config.entity.spec.NoIdSpec
import pl.exsio.querydsl.entityql.config.entity.spec.NoTableSpec
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
        Q<Spec> spec = qEntity(Spec)

        then:
        spec.longNumber("id") instanceof NumberPath<Long>
        spec.string("string") instanceof StringPath
        spec.decimalNumber("decimalNumber") instanceof NumberPath<BigDecimal>
        spec.shortNumber("shortNumber") instanceof NumberPath<Short>
        spec.doubleNumber("doubleNumber") instanceof NumberPath<Double>
        spec.floatNumber("floatNumber") instanceof NumberPath<Float>
        spec.byteNumber("byteNumber") instanceof NumberPath<Byte>
        spec.intNumber("intNumber") instanceof NumberPath<Integer>
        spec.bigIntNumber("bigintNumber") instanceof NumberPath<BigInteger>
        spec.date("date") instanceof DatePath<LocalDate>
        spec.dateTime("dateTime") instanceof DateTimePath<LocalDateTime>
        spec.<Integer> number("intNumber") instanceof NumberPath<Integer>
        spec.<LocalDate> column("date") instanceof DatePath<LocalDate>
        spec.<LocalDate> comparableColumn("date") instanceof DatePath<LocalDate>
    }

    def "should correctly throw exception when non existent column is fetched"() {
        given:
        Q<Spec<String>> spec = qEntity(Spec)
        when:
        spec.longNumber("nonExistentColumn")

        then:
        thrown InvalidArgumentException
    }

    def "should correctly get existing Foreign Keys"() {
        when:
        Q<SubSpec> subSpec = qEntity(SubSpec)

        then:
        subSpec.<Spec<String>> joinColumn("spec") instanceof ForeignKey<Spec>
    }

    def "should correctly throw exception when non existent Foreign Key is fetched"() {
        given:
        Q<SubSpec> subSpec = qEntity(SubSpec)

        when:
        subSpec.<Spec<String>> joinColumn("nonExistentFk")

        then:
        thrown InvalidArgumentException
    }

    def "should correctly get Primary Key"() {
        when:
        Q<SubSpec> subSpec = qEntity(SubSpec)

        then:
        subSpec.primaryKey() instanceof PrimaryKey<SubSpec>
    }

    def "should correctly throw exception on invalid Primary Key"() {
        when:
        qEntity(NoIdSpec)

        then:
        thrown MissingIdException
    }

    def "should correctly throw exception on missing @Table"() {
        when:
        qEntity(NoTableSpec)

        then:
        thrown InvalidArgumentException
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

    def "should correctly handle columns"() {
        when:
        Q<Spec> spec = qEntity(Spec)
        Q<SubSpec> subSpec = qEntity(SubSpec)

        then:
        spec.containsColumn("intNumber")
        !spec.containsColumn("noNumber")

        subSpec.containsJoinColumn("spec")
        !subSpec.containsJoinColumn("fk")

        spec.columns().size() == 15
        subSpec.joinColumns().size() == 1

    }
}
