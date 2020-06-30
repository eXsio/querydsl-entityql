package pl.exsio.querydsl.entityql.kotlin.jpa.it.generated

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
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.generated.QKCompositeFk
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.generated.QKCompositePk
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.generated.QKSingularPk
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQJPACompositeFkGeneratedIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnACompositeFKJoinToCompositePK() {
        //given:
        val compositePk = QKCompositePk.instance
        val compositeFk = QKCompositeFk.instance

        //when:
        val pks = queryFactory!!.query()
                .select(
                        constructor(
                                KCompositePk::class.java,
                                compositePk.id1,
                                compositePk.id2,
                                compositePk.desc
                        ))
                .from(compositeFk)
                .innerJoin(compositeFk.compositePk, compositePk)
                .where(compositeFk.desc.eq("fkd2"))
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
        val singularPk = QKSingularPk.instance
        val compositeFk = QKCompositeFk.instance

        //when:
        val pks = queryFactory!!.query()
                .select(
                        constructor(
                                KSingularPk::class.java,
                                singularPk.id1,
                                singularPk.id2,
                                singularPk.desc
                        ))
                .from(compositeFk)
                .innerJoin(compositeFk.singularPk, singularPk)
                .where(compositeFk.desc.eq("fkd2"))
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
        val compositePk = QKCompositePk.instance
        val compositeFk = QKCompositeFk.instance

        //when:
        val pks = queryFactory!!.query()
                .select(
                        constructor(
                                KCompositePk::class.java,
                                compositePk.id1,
                                compositePk.id2,
                                compositeFk.desc
                        ))
                .from(compositePk)
                .innerJoin(compositePk.compositeFks, compositeFk)
                .where(compositeFk.desc.eq("fkd2"))
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
        val singularPk = QKSingularPk.instance
        val compositeFk = QKCompositeFk.instance

        //when:
        val pks = queryFactory!!.query()
                .select(
                        constructor(
                                KSingularPk::class.java,
                                singularPk.id1,
                                singularPk.id2,
                                compositeFk.desc
                        ))
                .from(singularPk)
                .innerJoin(singularPk.compositeFks, compositeFk)
                .where(compositeFk.desc.eq("fkd2"))
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
