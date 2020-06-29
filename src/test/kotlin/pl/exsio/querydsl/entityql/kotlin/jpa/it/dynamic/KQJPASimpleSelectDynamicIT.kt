package pl.exsio.querydsl.entityql.kotlin.jpa.it.dynamic

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQueryFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.config.dto.KUserDto
import pl.exsio.querydsl.entityql.kotlin.config.enums.by_name.KUserTypeByName
import pl.exsio.querydsl.entityql.kotlin.config.enums.by_ordinal.KUserTypeByOrdinal
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KBook
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KUser
import java.util.Date
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQJPASimpleSelectDynamicIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldGetAllRowsFromAnEntity() {
        // given:
        val book = qEntity(KBook::class.java)
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(book).fetch()

        //then:
        assertEquals(books.size, 9)
        books.forEach { p ->
            assertNotNull(p.id)
            assertNotNull(p.name)
            assertNotNull(p.desc)
            assertNotNull(p.price)
        }
    }

    @Test
    fun shouldGetOneRowFromAnEntity() {
        // given:
        val book = qEntity(KBook::class.java)

        //when:
        val p = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .where(book.longNumber("id").eq(1L))
                .from(book).fetchOne()

        //then:
        assertNotNull(p)
        assertNotNull(p.id)
        assertNotNull(p.price)
        assertNotNull(p.desc)
        assertNotNull(p.name)
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAnEnumStringfilter() {
        // given:
        val user = qEntity(KUser::class.java)

        //when:
        val userName = queryFactory!!.query()
                .select(user.string("name"))
                .where(user.enumerated<KUserTypeByName>("typeStr").eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertEquals(userName, "U1")
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAnEnumOrdinalfilter() {
        // given:
        val user = qEntity(KUser::class.java)

        //when:
        val userName = queryFactory!!.query()
                .select(user.string("name"))
                .where(user.enumerated<KUserTypeByOrdinal>("typeOrd").eq(KUserTypeByOrdinal.ADMIN))
                .from(user).fetchOne()

        //then:
        assertEquals(userName, "U1")
    }

    @Test
    fun shouldGetGenericFields() {
        // given:
        val user = qEntity(KUser::class.java)

        //when:
        val createdBy = queryFactory!!.query()
                .select(user.column<String>("createdBy"))
                .where(user.enumerated<KUserTypeByName>("typeStr").eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertEquals(createdBy, "ADMIN")
    }

    @Test
    fun shouldGetUnknownFields() {
        // given:
        val user = qEntity(KUser::class.java)

        //when:
        val createdBy = queryFactory!!.query()
                .select(user.column<Date>("createdAt"))
                .where(user.enumerated<KUserTypeByName>("typeStr").eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertNotNull(createdBy)
    }

    @Test
    fun shouldGetEnumFields() {
        //given:
        val user = qEntity(KUser::class.java)

        //when:
        val type = queryFactory!!.query()
                .select(user.enumerated<KUserTypeByName>("typeStr"))
                .where(user.enumerated<KUserTypeByName>("typeStr").eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertEquals(type, KUserTypeByName.ADMIN)
    }

    @Test
    fun shouldGetBooleanFields() {
        //given:
        val user = qEntity(KUser::class.java)

        //when:
        val enabled = queryFactory!!.query()
                .select(user.bool("enabled"))
                .where(user.enumerated<KUserTypeByName>("typeStr").eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertTrue(enabled)
    }

    @Test
    fun shouldGetEnumAndBooleanFieldsInDTOProjection() {
        // given:
        val user = qEntity(KUser::class.java)

        //when:
        val userDto = queryFactory!!.query()
                .select(
                        constructor(
                                KUserDto::class.java,
                                user.longNumber("id"),
                                user.string("name"),
                                user.enumerated<KUserTypeByName>("typeStr"),
                                user.bool("enabled")))
                .where(user.enumerated<KUserTypeByName>("typeStr").eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertNotNull(userDto)
        assertTrue(userDto.enabled)
        assertEquals(userDto.type, KUserTypeByName.ADMIN)
    }
}
