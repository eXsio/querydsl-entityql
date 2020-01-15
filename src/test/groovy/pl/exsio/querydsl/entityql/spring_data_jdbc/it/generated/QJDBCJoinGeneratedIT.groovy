package pl.exsio.querydsl.entityql.spring_data_jdbc.it.generated

import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.config.SpringContext
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it.Book
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it.generated.QBook
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it.generated.QOrder
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it.generated.QOrderItem
import spock.lang.Specification

import static com.querydsl.core.types.Projections.constructor

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QJDBCJoinGeneratedIT extends Specification {

    @Autowired
    SQLQueryFactory queryFactory

    def "should get all rows from an Entity based on a Column / ON Join"() {
        given:
        QBook book = QBook.INSTANCE
        QOrder order = QOrder.INSTANCE
        QOrderItem orderItem = QOrderItem.INSTANCE

        when:
        List<Book> books = queryFactory.query()
                .select(
                        constructor(
                                Book,
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
        QBook book = QBook.INSTANCE
        QOrder order = QOrder.INSTANCE
        QOrderItem orderItem = QOrderItem.INSTANCE

        when:
        List<Book> books = queryFactory.query()
                .select(
                        constructor(
                                Book,
                                book.id,
                                book.name,
                                book.desc,
                                book.price
                        )
                )
                .from(book)
                .innerJoin(orderItem).on(orderItem.bookId.eq(book.id))
                .innerJoin(order).on(orderItem.orderId.eq(order.id))
                .where(order.id.eq(1L))
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
        QBook book = QBook.INSTANCE
        QOrder order = QOrder.INSTANCE
        QOrderItem orderItem = QOrderItem.INSTANCE

        when:
        List<Book> books = queryFactory.query()
                .select(
                        constructor(
                                Book,
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
