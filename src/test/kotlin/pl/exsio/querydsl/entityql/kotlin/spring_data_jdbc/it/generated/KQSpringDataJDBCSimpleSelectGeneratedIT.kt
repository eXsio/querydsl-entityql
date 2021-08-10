package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.it.generated

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQueryFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.config.dto.KUserDto
import pl.exsio.querydsl.entityql.kotlin.config.enums.by_name.KUserTypeByName
import pl.exsio.querydsl.entityql.kotlin.config.enums.by_ordinal.KUserTypeByOrdinal
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KBook
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated.QKBook
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated.QKUser
import kotlin.test.assertNotNull

@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQSpringDataJDBCSimpleSelectGeneratedIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldGetAllRowsFromAnEntity() {
        // given:
        val book = QKBook.instance
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.id,
                                book.name,
                                book.desc,
                                book.price
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
        val book = QKBook.instance

        //when:
        val p = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.id,
                                book.name,
                                book.desc,
                                book.price
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
        val user = QKUser.instance

        //when:
        val userName = queryFactory!!.query()
                .select(user.name)
                .where(user.typeStr.eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertEquals(userName, "U1")
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAnEnumOrdinalfilter() {
        // given:
        val user = QKUser.instance

        //when:
        val userName = queryFactory!!.query()
                .select(user.name)
                .where(user.typeOrd.eq(KUserTypeByOrdinal.ADMIN))
                .from(user).fetchOne()

        //then:
        assertEquals(userName, "U1")
    }

    @Test
    fun shouldGetGenericFields() {
        // given:
        val user = QKUser.instance

        //when:
        val createdBy = queryFactory!!.query()
                .select(user.createdBy)
                .where(user.typeStr.eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertEquals(createdBy, "ADMIN")
    }

    @Test
    fun shouldGetUnknownFields() {
        // given:
        val user = QKUser.instance

        //when:
        val createdBy = queryFactory!!.query()
                .select(user.createdAt)
                .where(user.typeStr.eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertNotNull(createdBy)
    }

    @Test
    fun shouldGetEnumFields() {
        //given:
        val user = QKUser.instance

        //when:
        val type = queryFactory!!.query()
                .select(user.typeStr)
                .where(user.typeStr.eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertEquals(type, KUserTypeByName.ADMIN)
    }

    @Test
    fun shouldGetBooleanFields() {
        //given:
        val user = QKUser.instance

        //when:
        val enabled = queryFactory!!.query()
                .select(user.enabled)
                .where(user.typeStr.eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertTrue(enabled)
    }

    @Test
    fun shouldGetEnumAndBooleanFieldsInDTOProjection() {
        // given:
        val user = QKUser.instance

        //when:
        val userDto = queryFactory!!.query()
                .select(
                        constructor(
                                KUserDto::class.java,
                                user.id,
                                user.name,
                                user.typeStr,
                                user.enabled))
                .where(user.typeStr.eq(KUserTypeByName.ADMIN))
                .from(user).fetchOne()

        //then:
        assertNotNull(userDto)
        assertTrue(userDto.enabled)
        assertEquals(userDto.type, KUserTypeByName.ADMIN)
    }
}
