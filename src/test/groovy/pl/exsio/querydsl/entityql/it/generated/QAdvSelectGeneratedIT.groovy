package pl.exsio.querydsl.entityql.it.generated

import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.config.SpringContext
import pl.exsio.querydsl.entityql.config.dto.OrderItemBookCountDto
import pl.exsio.querydsl.entityql.jpa.entity.it.generated.QBook
import pl.exsio.querydsl.entityql.jpa.entity.it.generated.QOrder
import pl.exsio.querydsl.entityql.jpa.entity.it.generated.QOrderItem
import pl.exsio.querydsl.entityql.jpa.entity.it.generated.QUser
import spock.lang.Specification

import static com.querydsl.core.types.Projections.constructor
import static com.querydsl.sql.SQLExpressions.count
import static com.querydsl.sql.SQLExpressions.select

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QAdvSelectGeneratedIT extends Specification {

    @Autowired
    SQLQueryFactory queryFactory

    def "should correctly use aggregate functions"() {
        given:
        QOrderItem orderItem = QOrderItem.INSTANCE

        when:

        List<OrderItemBookCountDto> result = queryFactory
                .select(
                        constructor(
                                OrderItemBookCountDto,
                                orderItem.orderId,
                                count(orderItem.bookId)))
                .from(orderItem)
                .groupBy(orderItem.orderId)
                .orderBy(orderItem.orderId.asc())
                .fetch();

        then:
        result.size() == 3
        result.get(0).orderId == 1L
        result.get(0).bookCount == 3L
        result.get(1).orderId == 2L
        result.get(1).bookCount == 5L
        result.get(2).orderId == 3L
        result.get(2).bookCount == 2L
    }

    def "should correctly use subQueries"() {
        given:
        QUser user = QUser.INSTANCE
        QBook book = QBook.INSTANCE
        QOrder order = QOrder.INSTANCE
        QOrderItem orderItem = QOrderItem.INSTANCE

        when:
        List<String> result = queryFactory.select(user.name)
                .from(user)
                .where(user.id.in(
                        select(order.userId)
                                .from(orderItem)
                                .innerJoin(orderItem.book, book)
                                .innerJoin(orderItem.order, order)
                                .where(book.price.gt(new BigDecimal("80")))
                )).fetch()

        then:
        result.size() == 2

    }

    def "should correctly use nested selects"() {
        given:
        QBook book = QBook.INSTANCE
        QOrder order = QOrder.INSTANCE
        QOrderItem orderItem = QOrderItem.INSTANCE

        when:
        Long result = queryFactory.select(count())
                .from(
                        select(order.userId)
                                .from(orderItem)
                                .innerJoin(orderItem.book, book)
                                .innerJoin(orderItem.order, order)
                                .where(book.price.gt(new BigDecimal("80")))
                ).fetchOne()

        then:
        result == 2L
    }
}
