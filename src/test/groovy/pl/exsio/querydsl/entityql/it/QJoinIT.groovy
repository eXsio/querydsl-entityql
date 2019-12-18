package pl.exsio.querydsl.entityql.it

import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.config.SpringContext
import pl.exsio.querydsl.entityql.config.entity.it.*
import spock.lang.Specification

import static com.querydsl.core.types.Projections.constructor
import static pl.exsio.querydsl.entityql.EntityQL.dto
import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QJoinIT extends Specification {

    @Autowired
    SQLQueryFactory queryFactory

    def "should get all rows from an Entity based on a Column / ON Join"() {
        given:
        Q<Book> book = qEntity(Book)
        Q<Order> order = qEntity(Order)
        Q<OrderItem> orderItem = qEntity(OrderItem)

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
                .innerJoin(orderItem).on(orderItem.longNumber("book").eq(book.longNumber("id")))
                .innerJoin(order).on(orderItem.longNumber("order").eq(order.longNumber("id")))
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
        Q<Book> book = qEntity(Book)
        Q<Order> order = qEntity(Order)
        Q<OrderItem> orderItem = qEntity(OrderItem)

        when:
        List<Book> books = queryFactory.query()
                .select(
                        dto(Book, book.columns("id", "name", "desc", "price"))
                )
                .from(book)
                .innerJoin(orderItem).on(orderItem.longNumber("book").eq(book.longNumber("id")))
                .innerJoin(order).on(orderItem.longNumber("order").eq(order.longNumber("id")))
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
        Q<Book> book = qEntity(Book)
        Q<Order> order = qEntity(Order)
        Q<OrderItem> orderItem = qEntity(OrderItem)

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

    def "should get all rows from an Entity based on a FK Join with custom referencedColumnName"() {
        given:
        Q<Group> group = qEntity(Group)
        Q<GroupAdmin> groupAdmin = qEntity(GroupAdmin)

        when:
        List<Group> groups = queryFactory.query()
                .select(
                        constructor(
                                Group,
                                group.longNumber("id"),
                                group.string("name")
                        ))
                .from(group)
                .innerJoin(group.<GroupAdmin> joinColumn("admin"), groupAdmin)
                .where(groupAdmin.longNumber("id").eq(2L))
                .fetch()

        then:
        groups.size() == 1
        groups.forEach { g ->
            assert g.id == 2
            assert g.name == "G2"
        }
    }

    def "should get all rows from an Entity based on a Join Table mapping"() {
        given:
        Q<Group> group = qEntity(Group)
        Q<User> user = qEntity(User)
        Q<UserGroup> userGroup = qEntity(UserGroup)

        when:
        List<Group> groups = queryFactory.query()
                .select(
                        constructor(
                                Group,
                                group.longNumber("id"),
                                group.string("name")
                        ))
                .from(group)
                .innerJoin(userGroup).on(group.longNumber("id").eq(userGroup.longNumber("groupId")))
                .innerJoin(user).on(userGroup.longNumber("userId").eq(user.longNumber("id")))
                .where(user.longNumber("id").eq(2L))
                .fetch()

        then:
        groups.size() == 1
        groups.forEach { g ->
            assert g.id == 1
            assert g.name == "G1"
        }
    }
}
