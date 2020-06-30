package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.it.dynamic


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
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.entity.scanner.SpringDataJdbcQEntityScanner
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.config.dto.KOrderItemBookCountDto
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KBook
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KOrder
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KOrderItem
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KUser
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KUpperCaseWithUnderscoresNamingStrategy
import java.math.BigDecimal
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQSpringDataJDBCAdvSelectDynamicIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    var scanner = SpringDataJdbcQEntityScanner(KUpperCaseWithUnderscoresNamingStrategy())

    @Test
    fun shouldCorrectlyUseAggregateFunctions() {
        //given:
        val orderItem = qEntity(KOrderItem::class.java, scanner)

        //when:

        val result = queryFactory!!
                .select(
                        constructor(
                                KOrderItemBookCountDto::class.java,
                                orderItem.longNumber("orderId"),
                                count(orderItem.longNumber("bookId"))))
                .from(orderItem)
                .groupBy(orderItem.longNumber("orderId"))
                .orderBy(orderItem.longNumber("orderId").asc())
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
        val user = qEntity(KUser::class.java, scanner)
        val book = qEntity(KBook::class.java, scanner)
        val order = qEntity(KOrder::class.java, scanner)
        val orderItem = qEntity(KOrderItem::class.java, scanner)

        //when:
        val result = queryFactory!!
                .select(user.string("name"))
                .from(user)
                .where(user.longNumber("id").`in`(
                        select(order.longNumber("userId"))
                                .from(orderItem)
                                .innerJoin(orderItem.joinColumn<KBook>("book"), book)
                                .innerJoin(orderItem.joinColumn<KOrder>("order"), order)
                                .where(book.decimalNumber("price").gt(BigDecimal("80")))
                )).fetch()

        //then:
        assertEquals(result.size, 2)

    }

    @Test
    fun shouldCorrectlyUseNestedSelects() {
        // given:
        val book = qEntity(KBook::class.java, scanner)
        val order = qEntity(KOrder::class.java, scanner)
        val orderItem = qEntity(KOrderItem::class.java, scanner)

        //when:
        val result = queryFactory!!
                .select(count(Wildcard.all))
                .from(
                        select(order.longNumber("userId"))
                                .from(orderItem)
                                .innerJoin(orderItem.joinColumn<KBook>("book"), book)
                                .innerJoin(orderItem.joinColumn<KOrder>("order"), order)
                                .where(book.decimalNumber("price").gt(BigDecimal("80")))
                ).fetchOne()

        //then:
        assertEquals(result, 2L)
    }
}
