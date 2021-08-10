package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.it.generated

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQueryFactory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.EntityQL.dto
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KBook
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KGroup
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQSpringDataJDBCJoinGeneratedIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAColumnONJoin() {
        //given:
        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.id,
                                book.name,
                                book.desc,
                                book.price
                        ))
                .from(book)
                .innerJoin(orderItem).on(orderItem.bookId.eq(book.id))
                .innerJoin(order).on(orderItem.orderId.eq(order.id))
                .where(order.id.eq(1L))
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
        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val books = queryFactory!!.query()
                .select(
                        dto(KBook::class.java, listOf(book.id, book.name, book.desc, book.price))
                )
                .from(book)
                .innerJoin(orderItem).on(orderItem.bookId.eq(book.id))
                .innerJoin(order).on(orderItem.orderId.eq(order.id))
                .where(order.id.eq(1L))
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
        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.id,
                                book.name,
                                book.desc,
                                book.price
                        ))
                .from(orderItem)
                .innerJoin(orderItem.book, book)
                .innerJoin(orderItem.order, order)
                .where(order.id.eq(2L))
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
