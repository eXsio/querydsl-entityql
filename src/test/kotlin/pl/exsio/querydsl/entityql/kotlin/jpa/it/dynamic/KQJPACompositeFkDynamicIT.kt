package pl.exsio.querydsl.entityql.kotlin.jpa.it.dynamic

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQueryFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KCompositeFk
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KCompositePk
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KSingularPk
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQJPACompositeFkDynamicIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnACompositeFKJoinToCompositePK() {
        //given:
        val compositePk = qEntity(KCompositePk::class.java)
        val compositeFk = qEntity(KCompositeFk::class.java)

        //when:
        val pks = queryFactory!!.query()
                .select(
                        constructor(
                                KCompositePk::class.java,
                                compositePk.longNumber("id1"),
                                compositePk.string("id2"),
                                compositePk.string("desc")
                        ))
                .from(compositeFk)
                .innerJoin(compositeFk.joinColumn<KCompositePk>("compositePk"), compositePk)
                .where(compositeFk.string("desc").eq("fkd2"))
                .fetch()

        //then:
        assertEquals(pks.size, 1)
        pks.forEach { pk ->
            assertEquals(pk.id1, 2L)
            assertEquals(pk.id2, "s2")
            assertEquals(pk.desc, "pkd2")
        }
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnACompositeFKJoinToSingularPK() {
        //given:
        val singularPk = qEntity(KSingularPk::class.java)
        val compositeFk = qEntity(KCompositeFk::class.java)

        //when:
        val pks = queryFactory!!.query()
                .select(
                        constructor(
                                KSingularPk::class.java,
                                singularPk.longNumber("id1"),
                                singularPk.string("id2"),
                                singularPk.string("desc")
                        ))
                .from(compositeFk)
                .innerJoin(compositeFk.joinColumn<KSingularPk>("singularPk"), singularPk)
                .where(compositeFk.string("desc").eq("fkd2"))
                .fetch()

        //then:
        assertEquals(pks.size, 1)
        pks.forEach { pk ->
            assertEquals(pk.id1, 2L)
            assertEquals(pk.id2, "s2")
            assertEquals(pk.desc, "pkd2")
        }
    }

    @Test
    fun shouldGetAllRowsGromAnEntityBasedOnAnInverseCompositeFKJoinToCompositePK() {
        //given:
        val compositePk = qEntity(KCompositePk::class.java)
        val compositeFk = qEntity(KCompositeFk::class.java)

        //when:
        val pks = queryFactory!!.query()
                .select(
                        constructor(
                                KCompositePk::class.java,
                                compositePk.longNumber("id1"),
                                compositePk.string("id2"),
                                compositeFk.string("desc")
                        ))
                .from(compositePk)
                .innerJoin(compositePk.inverseJoinColumn<KCompositeFk>("compositeFks"), compositeFk)
                .where(compositeFk.string("desc").eq("fkd2"))
                .fetch()

        //then:
        assertEquals(pks.size, 1)
        pks.forEach { pk ->
            assertEquals(pk.id1, 2L)
            assertEquals(pk.id2, "s2")
            assertEquals(pk.desc, "fkd2")
        }
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAnInverseCompositeFKJoinToSingularPK() {
        //given:
        val singularPk = qEntity(KSingularPk::class.java)
        val compositeFk = qEntity(KCompositeFk::class.java)

        //when:
        val pks = queryFactory!!.query()
                .select(
                        constructor(
                                KSingularPk::class.java,
                                singularPk.longNumber("id1"),
                                singularPk.string("id2"),
                                compositeFk.string("desc")
                        ))
                .from(singularPk)
                .innerJoin(singularPk.inverseJoinColumn<KCompositeFk>("compositeFks"), compositeFk)
                .where(compositeFk.string("desc").eq("fkd2"))
                .fetch()

        //then:
        assertEquals(pks.size, 1)
        pks.forEach { pk ->
            assertEquals(pk.id1, 2L)
            assertEquals(pk.id2, "s2")
            assertEquals(pk.desc, "fkd2")
        }
    }

}
