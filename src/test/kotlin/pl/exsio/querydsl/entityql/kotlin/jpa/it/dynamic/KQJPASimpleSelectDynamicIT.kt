package pl.exsio.querydsl.entityql.kotlin.jpa.it.dynamic

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQueryFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KBook
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQJPASimpleSelectDynamicIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldGetAllRowsFromAnEntity() {
        val book = qEntity(KBook::class.java)
        println(queryFactory != null)
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

        assertEquals(books.size, 9)
        books.forEach { p ->
            assertNotNull(p.id)
            assertNotNull(p.name)
            assertNotNull(p.desc)
            assertNotNull(p.price)
        }
    }

//    def "should get one row from an Entity"() {
//        given:
//        Q<Book> book = qEntity(Book)
//
//        when:
//        Book p = queryFactory.query()
//                .select(
//                        constructor(
//                                Book,
//                                book.longNumber("id"),
//                                book.string("name"),
//                                book.string("desc"),
//                                book.decimalNumber("price")
//                        ))
//                .where(book.longNumber("id").eq(1L))
//                .from(book).fetchOne()
//
//        then:
//        p != null
//        p.id != null
//        p.price != null
//        p.desc != null
//        p.name != null
//    }
//
//    def "should get all rows from an Entity based on an Enum String filter"() {
//        given:
//        Q<User<String>> user = qEntity(User)
//
//        when:
//        String userName = queryFactory.query()
//                .select(user.string("name"))
//                .where(user.<KUserTypeByName> enumerated("typeStr").eq(KUserTypeByName.ADMIN))
//                .from(user).fetchOne()
//
//        then:
//        userName == "U1"
//    }
//
//    def "should get all rows from an Entity based on an Enum Ordinal filter"() {
//        given:
//        Q<User<String>> user = qEntity(User)
//
//        when:
//        String userName = queryFactory.query()
//                .select(user.string("name"))
//                .where(user.<KUserTypeByOrdinal>enumerated("typeOrd").eq(KUserTypeByOrdinal.ADMIN))
//                .from(user).fetchOne()
//
//        then:
//        userName == "U1"
//    }
//
//    def "should get generic Fields"() {
//        given:
//        Q<User<String>> user = qEntity(User)
//
//        when:
//        String createdBy = queryFactory.query()
//                .select(user.<String> column("createdBy"))
//                .where(user.<KUserTypeByName> enumerated("typeStr").eq(KUserTypeByName.ADMIN))
//                .from(user).fetchOne()
//
//        then:
//        createdBy == "ADMIN"
//    }
//
//    def "should get unknown Fields"() {
//        given:
//        Q<User<String>> user = qEntity(User)
//
//        when:
//        Date createdBy = queryFactory.query()
//                .select(user.<Date> column("createdAt"))
//                .where(user.<KUserTypeByName> enumerated("typeStr").eq(KUserTypeByName.ADMIN))
//                .from(user).fetchOne()
//
//        then:
//        createdBy != null
//    }
//
//    def "should get enum Fields"() {
//        given:
//        Q<User> user = qEntity(User)
//
//        when:
//        KUserTypeByName type = queryFactory.query()
//                .select(user.<KUserTypeByName>enumerated("typeStr"))
//                .where(user.<KUserTypeByName>enumerated("typeStr").eq(KUserTypeByName.ADMIN))
//                .from(user).fetchOne()
//
//        then:
//        type == KUserTypeByName.ADMIN
//    }
//
//    def "should get boolean Fields"() {
//        given:
//        Q<User> user = qEntity(User)
//
//        when:
//        Boolean enabled = queryFactory.query()
//                .select(user.bool("enabled"))
//                .where(user.<KUserTypeByName>enumerated("typeStr").eq(KUserTypeByName.ADMIN))
//                .from(user).fetchOne()
//
//        then:
//        enabled
//    }
//
//    def "should get enum and boolean Fields in DTO projection"() {
//        given:
//        Q<User> user = qEntity(User)
//
//        when:
//        KUserDto userDto = queryFactory.query()
//                .select(constructor(KUserDto, user.longNumber("id"), user.string("name"), user.<KUserTypeByName>enumerated("typeStr"), user.bool("enabled")))
//                .where(user.<KUserTypeByName>enumerated("typeStr").eq(KUserTypeByName.ADMIN))
//                .from(user).fetchOne()
//
//        then:
//        userDto != null
//        userDto.enabled
//        userDto.type == KUserTypeByName.ADMIN
//    }
}
