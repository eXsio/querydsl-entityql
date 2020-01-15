package pl.exsio.querydsl.entityql.spring_data_jdbc.it.dynamic

import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.config.SpringContext
import pl.exsio.querydsl.entityql.entity.scanner.SpringDataJdbcQEntityScanner
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.UpperCaseWithUnderscoresNamingStrategy
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it.Book
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it.Order
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it.OrderItem
import spock.lang.Specification

import static com.querydsl.core.types.Projections.constructor
import static pl.exsio.querydsl.entityql.EntityQL.dto
import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QJDBCJoinDynamicIT extends Specification {

    @Autowired
    SQLQueryFactory queryFactory

    SpringDataJdbcQEntityScanner scanner = new SpringDataJdbcQEntityScanner(new UpperCaseWithUnderscoresNamingStrategy());

    def "should get all rows from an Entity based on a Column / ON Join"() {
        given:
        Q<Book> book = qEntity(Book, scanner)
        Q<Order> order = qEntity(Order, scanner)
        Q<OrderItem> orderItem = qEntity(OrderItem, scanner)

        when:
        List<Book> books = queryFactory.query()
                .select(
                        constructor(
                                Book,
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

        then:
        books.size() == 3
        books.forEach { p ->
            assert p.id != null
            assert p.name != null
            assert p.desc != null
            assert p.price != null
        }
    }

    def "should get all rows using list of column names"() {
        given:
        Q<Book> book = qEntity(Book, scanner)
        Q<Order> order = qEntity(Order, scanner)
        Q<OrderItem> orderItem = qEntity(OrderItem, scanner)

        when:
        List<Book> books = queryFactory.query()
                .select(
                        dto(Book, book.columns("id", "name", "desc", "price"))
                )
                .from(book)
                .innerJoin(orderItem).on(orderItem.longNumber("bookId").eq(book.longNumber("id")))
                .innerJoin(order).on(orderItem.longNumber("orderId").eq(order.longNumber("id")))
                .where(order.longNumber("id").eq(1L))
                .fetch()

        then:
        books.size() == 3
        books.forEach { p ->
            assert p.id != null
            assert p.name != null
            assert p.desc != null
            assert p.price != null
        }
    }

    def "should get all rows from an Entity based on a FK Join"() {
        given:
        Q<Book> book = qEntity(Book, scanner)
        Q<Order> order = qEntity(Order, scanner)
        Q<OrderItem> orderItem = qEntity(OrderItem, scanner)

        when:
        List<Book> books = queryFactory.query()
                .select(
                        constructor(
                                Book,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(orderItem)
                .innerJoin(orderItem.<Book> joinColumn("book"), book)
                .innerJoin(orderItem.<Order> joinColumn("order"), order)
                .where(order.longNumber("id").eq(2L))
                .fetch()

        then:
        books.size() == 5
        books.forEach { p ->
            assert p.id != null
            assert p.name != null
            assert p.desc != null
            assert p.price != null
        }
    }

}
