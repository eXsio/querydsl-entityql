package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.it.generated


import com.querydsl.core.types.ExpressionUtils.count
import com.querydsl.core.types.Projections.constructor
import com.querydsl.core.types.dsl.Wildcard
import com.querydsl.sql.SQLExpressions.select
import com.querydsl.sql.SQLQueryFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.config.dto.KOrderItemBookCountDto
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated.QKBook
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated.QKOrder
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated.QKOrderItem
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated.QKUser
import java.math.BigDecimal
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQSpringDataJDBCAdvSelectGeneratedIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldCorrectlyUseAggregateFunctions() {
        //given:
        val orderItem = QKOrderItem.instance

        //when:

        val result = queryFactory!!
                .select(
                        constructor(
                                KOrderItemBookCountDto::class.java,
                                orderItem.orderId,
                                count(orderItem.bookId)))
                .from(orderItem)
                .groupBy(orderItem.orderId)
                .orderBy(orderItem.orderId.asc())
                .fetch();

        //then:
        assertEquals(result.size, 3)
        assertEquals(result.get(0).orderId, 1L)
        assertEquals(result.get(0).bookCount, 3L)
        assertEquals(result.get(1).orderId, 2L)
        assertEquals(result.get(1).bookCount, 5L)
        assertEquals(result.get(2).orderId, 3L)
        assertEquals(result.get(2).bookCount, 2L)
    }

    @Test
    fun shouldCorrectlyUseSubQueries() {
        //given:
        val user = QKUser.instance
        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val result = queryFactory!!
                .select(user.name)
                .from(user)
                .where(user.id.`in`(
                        select(order.userId)
                                .from(orderItem)
                                .innerJoin(orderItem.book, book)
                                .innerJoin(orderItem.order, order)
                                .where(book.price.gt(BigDecimal("80")))
                )).fetch()

        //then:
        assertEquals(result.size, 2)

    }

    @Test
    fun shouldCorrectlyUseNestedSelects() {
        // given:
        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val result = queryFactory!!
                .select(count(Wildcard.all))
                .from(
                        select(order.userId)
                                .from(orderItem)
                                .innerJoin(orderItem.book, book)
                                .innerJoin(orderItem.order, order)
                                .where(book.price.gt(BigDecimal("80")))
                ).fetchOne()

        //then:
        assertEquals(result, 2L)
    }
}
