package pl.exsio.querydsl.entityql.groovy.runtime.it

import com.querydsl.core.Tuple
import com.querydsl.core.types.Path
import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.groovy.config.SpringContext
import pl.exsio.querydsl.entityql.type.SimpleMapProjections

import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QRuntimeJoinDynamicIT extends RuntimeFixedTestBase {

    @Autowired
    SQLQueryFactory queryFactory

    def "should get all rows from an Entity based on a Column / ON Join"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)
        Q<Tuple> order = qEntity(TABLE_ORDERS)
        Q<Tuple> orderItem = qEntity(TABLE_ORDER_ITEMS)

        when:
        List<Map<String, ?>> books = queryFactory.query()
                .select(
                        SimpleMapProjections.map(
                                book.longNumber("bookId"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(book)
                .innerJoin(orderItem).on(orderItem.longNumber("bookId").eq(book.longNumber("bookId")))
                .innerJoin(order).on(orderItem.longNumber("itemOrderId").eq(order.longNumber("orderId")))
                .where(order.longNumber("orderId").eq(1L))
                .fetch()

        then:
        books.size() == 3
        books.forEach { p ->
            assert p.bookId != null
            assert p.name != null
            assert p.desc != null
            assert p.price != null
        }
    }

    def "should get all rows using list of column names"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)
        Q<Tuple> order = qEntity(TABLE_ORDERS)
        Q<Tuple> orderItem = qEntity(TABLE_ORDER_ITEMS)

        when:
        List<Map<String, ?>> books = queryFactory.query()
                .select(
                    SimpleMapProjections.map(book.columns("bookId", "name", "desc", "price") as List<Path<?>>)
                )
                .from(book)
                .innerJoin(orderItem).on(orderItem.longNumber("bookId").eq(book.longNumber("bookId")))
                .innerJoin(order).on(orderItem.longNumber("itemOrderId").eq(order.longNumber("orderId")))
                .where(order.longNumber("orderId").eq(1L))
                .fetch()

        then:
        books.size() == 3
        books.forEach { p ->
            assert p.bookId != null
            assert p.name != null
            assert p.desc != null
            assert p.price != null
        }
    }

}
