package pl.exsio.querydsl.entityql.jpa.it.dynamic

import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.config.SpringContext
import pl.exsio.querydsl.entityql.jpa.entity.it.CompositeFk
import pl.exsio.querydsl.entityql.jpa.entity.it.CompositePk
import pl.exsio.querydsl.entityql.jpa.entity.it.SingularPk
import spock.lang.Specification

import static com.querydsl.core.types.Projections.constructor
import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QJPACompositeFkDynamicIT extends Specification {

    @Autowired
    SQLQueryFactory queryFactory


    def "should get all rows from an Entity based on a Composite FK Join to Composite PK"() {
        given:
        Q<CompositePk> compositePk = qEntity(CompositePk)
        Q<CompositeFk> compositeFk = qEntity(CompositeFk)

        when:
        List<CompositePk> pks = queryFactory.query()
                .select(
                        constructor(
                                CompositePk,
                                compositePk.longNumber("id1"),
                                compositePk.string("id2"),
                                compositePk.string("desc")
                        ))
                .from(compositeFk)
                .innerJoin(compositeFk.<CompositePk> joinColumn("compositePk"), compositePk)
                .where(compositeFk.string("desc").eq("fkd2"))
                .fetch()

        then:
        pks.size() == 1
        pks.forEach { pk ->
            assert pk.id1 == 2L
            assert pk.id2 == "s2"
            assert pk.desc == "pkd2"
        }
    }

    def "should get all rows from an Entity based on a Composite FK Join to Singular PK"() {
        given:
        Q<SingularPk> singularPk = qEntity(SingularPk)
        Q<CompositeFk> compositeFk = qEntity(CompositeFk)

        when:
        List<SingularPk> pks = queryFactory.query()
                .select(
                        constructor(
                                SingularPk,
                                singularPk.longNumber("id1"),
                                singularPk.string("id2"),
                                singularPk.string("desc")
                        ))
                .from(compositeFk)
                .innerJoin(compositeFk.<SingularPk> joinColumn("singularPk"), singularPk)
                .where(compositeFk.string("desc").eq("fkd2"))
                .fetch()

        then:
        pks.size() == 1
        pks.forEach { pk ->
            assert pk.id1 == 2L
            assert pk.id2 == "s2"
            assert pk.desc == "pkd2"
        }
    }


    def "should get all rows from an Entity based on an inverse Composite FK Join to Composite PK"() {
        given:
        Q<CompositePk> compositePk = qEntity(CompositePk)
        Q<CompositeFk> compositeFk = qEntity(CompositeFk)

        when:
        List<CompositePk> pks = queryFactory.query()
                .select(
                        constructor(
                                CompositePk,
                                compositePk.longNumber("id1"),
                                compositePk.string("id2"),
                                compositeFk.string("desc")
                        ))
                .from(compositePk)
                .innerJoin(compositePk.<CompositeFk> inverseJoinColumn("compositeFks"), compositeFk)
                .where(compositeFk.string("desc").eq("fkd2"))
                .fetch()

        then:
        pks.size() == 1
        pks.forEach { pk ->
            assert pk.id1 == 2L
            assert pk.id2 == "s2"
            assert pk.desc == "fkd2"
        }
    }

    def "should get all rows from an Entity based on an inverse Composite FK Join to Singular PK"() {
        given:
        Q<SingularPk> singularPk = qEntity(SingularPk)
        Q<CompositeFk> compositeFk = qEntity(CompositeFk)

        when:
        List<SingularPk> pks = queryFactory.query()
                .select(
                        constructor(
                                SingularPk,
                                singularPk.longNumber("id1"),
                                singularPk.string("id2"),
                                compositeFk.string("desc")
                        ))
                .from(singularPk)
                .innerJoin(singularPk.<CompositeFk> inverseJoinColumn("compositeFks"), compositeFk)
                .where(compositeFk.string("desc").eq("fkd2"))
                .fetch()

        then:
        pks.size() == 1
        pks.forEach { pk ->
            assert pk.id1 == 2L
            assert pk.id2 == "s2"
            assert pk.desc == "fkd2"
        }
    }

}
