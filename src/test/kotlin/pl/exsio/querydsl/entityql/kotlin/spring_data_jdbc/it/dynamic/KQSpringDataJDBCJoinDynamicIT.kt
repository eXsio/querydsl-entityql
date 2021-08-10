package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.it.dynamic

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQueryFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.EntityQL.dto
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.entity.scanner.SpringDataJdbcQEntityScanner
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.*
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KUpperCaseWithUnderscoresNamingStrategy
import kotlin.test.assertNotNull

@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQSpringDataJDBCJoinDynamicIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    var scanner = SpringDataJdbcQEntityScanner(KUpperCaseWithUnderscoresNamingStrategy())

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAColumnONJoin() {
        //given:
        val book = qEntity(KBook::class.java, scanner)
        val order = qEntity(KOrder::class.java, scanner)
        val orderItem = qEntity(KOrderItem::class.java, scanner)

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(book)
                .innerJoin(orderItem).on(orderItem.longNumber("bookId").eq(book.longNumber("id")))
                .innerJoin(order).on(orderItem.longNumber("orderId").eq(order.longNumber("id")))
                .where(order.longNumber("id").eq(1L))
                .fetch()

        //then:
        assertEquals(books.size, 3)
        books.forEach { b ->
            assertNotNull(b.id)
            assertNotNull(b.name)
            assertNotNull(b.desc)
            assertNotNull(b.price)
        }
    }

    @Test
    fun shouldGetAllRowsUsingListOfColumnNames() {
        //given:
        val book = qEntity(KBook::class.java, scanner)
        val order = qEntity(KOrder::class.java, scanner)
        val orderItem = qEntity(KOrderItem::class.java, scanner)

        //when:
        val books = queryFactory!!.query()
                .select(
                        dto(KBook::class.java, book.columns("id", "name", "desc", "price"))
                )
                .from(book)
                .innerJoin(orderItem).on(orderItem.longNumber("bookId").eq(book.longNumber("id")))
                .innerJoin(order).on(orderItem.longNumber("orderId").eq(order.longNumber("id")))
                .where(order.longNumber("id").eq(1L))
                .fetch()

        //then:
        assertEquals(books.size, 3)
        books.forEach { b ->
            assertNotNull(b.id)
            assertNotNull(b.name)
            assertNotNull(b.desc)
            assertNotNull(b.price)
        }
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnFKJoin() {
        //given:
        val book = qEntity(KBook::class.java, scanner)
        val order = qEntity(KOrder::class.java, scanner)
        val orderItem = qEntity(KOrderItem::class.java, scanner)

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(orderItem)
                .innerJoin(orderItem.joinColumn<KBook>("book"), book)
                .innerJoin(orderItem.joinColumn<KOrder>("order"), order)
                .where(order.longNumber("id").eq(2L))
                .fetch()

        //then:
        assertEquals(books.size, 5)
        books.forEach { b ->
            assertNotNull(b.id)
            assertNotNull(b.name)
            assertNotNull(b.desc)
            assertNotNull(b.price)
        }
    }
}
