package pl.exsio.querydsl.entityql.groovy.runtime.it

import com.querydsl.core.Tuple
import com.querydsl.core.types.Projections
import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.groovy.config.SpringContext

import static com.querydsl.sql.SQLExpressions.count
import static com.querydsl.sql.SQLExpressions.select
import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QRuntimeAdvSelectDynamicIT extends RuntimeFixedTestBase {

    @Autowired
    SQLQueryFactory queryFactory

    def "should correctly use aggregate functions"() {
        given:
        Q<Tuple> orderItem = qEntity(TABLE_ORDER_ITEMS)

        when:
        def itemId = orderItem.longNumber("itemOrderId")
        def bookCount = count(orderItem.longNumber("bookId")).as("bookCount")

        List<List<Long>> result = queryFactory
                .select(Projections.list(
                    itemId,
                    bookCount
                ))
                .from(orderItem)
                .groupBy(itemId)
                .orderBy(itemId.asc())
                .fetch()

        then:
        result.size() == 3
        result == [
                [1L, 3L]
                , [2L, 5L]
                , [3L, 2L]
        ]
    }

    def "should correctly use subQueries"() {
        given:
        Q<Tuple> user = qEntity(TABLE_USERS)
        Q<Tuple> book = qEntity(TABLE_BOOKS)
        Q<Tuple> order = qEntity(TABLE_ORDERS)
        Q<Tuple> orderItem = qEntity(TABLE_ORDER_ITEMS)

        def itemBookId = orderItem.longNumber("bookId")
        def bookId = book.longNumber("bookId")
        def itemOrderId = orderItem.longNumber("itemOrderId")
        def orderId = order.longNumber("orderId")
        def price = book.decimalNumber("price")

        when:
        List<String> result = queryFactory.select(user.string("name"))
                .from(user)
                .where(user.longNumber("userId").in(
                        select(order.longNumber("userId"))
                                .from(orderItem)
                                .innerJoin(book).on(itemBookId.eq(bookId))
                                .innerJoin(order).on(itemOrderId.eq(orderId))
                                .where(price.gt(new BigDecimal("80")))
                )).fetch()

        then:
        result.size() == 2
        result == ['U2', 'U3']
    }

    def "should correctly use nested selects"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)
        Q<Tuple> order = qEntity(TABLE_ORDERS)
        Q<Tuple> orderItem = qEntity(TABLE_ORDER_ITEMS)

        def itemBookId = orderItem.longNumber("bookId")
        def bookId = book.longNumber("bookId")
        def itemOrderId = orderItem.longNumber("itemOrderId")
        def orderId = order.longNumber("orderId")
        def price = book.decimalNumber("price")

        when:
        Long result = queryFactory.select(count())
                .from(
                        select(order.longNumber("userId"))
                                .from(orderItem)
                                .innerJoin(book).on(itemBookId.eq(bookId))
                                .innerJoin(order).on(itemOrderId.eq(orderId))
                                .where(price.gt(new BigDecimal("80")))
                ).fetchOne()

        then:
        result == 2L
    }

}
