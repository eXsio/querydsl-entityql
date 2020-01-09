package pl.exsio.querydsl.entityql.it


import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.config.SpringContext
import pl.exsio.querydsl.entityql.config.dto.OrderItemBookCountDto
import pl.exsio.querydsl.entityql.config.entity.it.Book
import pl.exsio.querydsl.entityql.config.entity.it.Order
import pl.exsio.querydsl.entityql.config.entity.it.OrderItem
import pl.exsio.querydsl.entityql.config.entity.it.User
import spock.lang.Specification

import static com.querydsl.core.types.Projections.constructor
import static com.querydsl.sql.SQLExpressions.count
import static com.querydsl.sql.SQLExpressions.select
import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QAdvSelectIT extends Specification {

    @Autowired
    SQLQueryFactory queryFactory

    def "should correctly use aggregate functions"() {
        given:
        Q<OrderItem> orderItem = qEntity(OrderItem)

        when:

        List<OrderItemBookCountDto> result = queryFactory
                .select(
                        constructor(
                                OrderItemBookCountDto,
                                orderItem.longNumber("orderId"),
                                count(orderItem.longNumber("bookId"))))
                .from(orderItem)
                .groupBy(orderItem.longNumber("orderId"))
                .orderBy(orderItem.longNumber("orderId").asc())
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
        Q<User> user = qEntity(User)
        Q<Book> book = qEntity(Book)
        Q<Order> order = qEntity(Order)
        Q<OrderItem> orderItem = qEntity(OrderItem)

        when:
        List<String> result = queryFactory.select(user.string("name"))
                .from(user)
                .where(user.longNumber("id").in(
                        select(order.longNumber("userId"))
                                .from(orderItem)
                                .innerJoin(orderItem.<Book> joinColumn("book"), book)
                                .innerJoin(orderItem.<Order> joinColumn("order"), order)
                                .where(book.decimalNumber("price").gt(new BigDecimal("80")))
                )).fetch()

        then:
        result.size() == 2

    }

    def "should correctly use nested selects"() {
        given:
        Q<Book> book = qEntity(Book)
        Q<Order> order = qEntity(Order)
        Q<OrderItem> orderItem = qEntity(OrderItem)

        when:
        Long result = queryFactory.select(count())
                .from(
                        select(order.longNumber("userId"))
                                .from(orderItem)
                                .innerJoin(orderItem.<Book> joinColumn("book"), book)
                                .innerJoin(orderItem.<Order> joinColumn("order"), order)
                                .where(book.decimalNumber("price").gt(new BigDecimal("80")))
                ).fetchOne()

        then:
        result == 2L
    }
}
