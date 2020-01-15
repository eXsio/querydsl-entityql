package pl.exsio.querydsl.entityql.it.generated

import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.config.SpringContext
import pl.exsio.querydsl.entityql.jpa.entity.it.Book
import pl.exsio.querydsl.entityql.jpa.entity.it.Group
import pl.exsio.querydsl.entityql.jpa.entity.it.generated.*
import spock.lang.Specification

import static com.querydsl.core.types.Projections.constructor

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QJoinGeneratedIT extends Specification {

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

    def "should get all rows from an Entity based on a FK Join with custom referencedColumnName"() {
        given:
        QGroup group = QGroup.INSTANCE
        QGroupAdmin groupAdmin = QGroupAdmin.INSTANCE

        when:
        List<Group> groups = queryFactory.query()
                .select(
                        constructor(
                                Group,
                                group.id,
                                group.name
                        ))
                .from(group)
                .innerJoin(group.admin, groupAdmin)
                .where(groupAdmin.id.eq(2L))
                .fetch()

        then:
        groups.size() == 1
        groups.forEach { g ->
            assert g.id == 2
            assert g.name == "G2"
        }
    }

    def "should get all rows from an Entity based on a Join Table mapping using ON clause"() {
        given:
        QGroup group = QGroup.INSTANCE
        QUser user = QUser.INSTANCE
        QUserGroup userGroup = QUserGroup.INSTANCE

        when:
        List<Group> groups = queryFactory.query()
                .select(
                        constructor(
                                Group,
                                group.id,
                                group.name
                        ))
                .from(userGroup)
                .innerJoin(group).on(userGroup.groupId.eq(group.id))
                .innerJoin(user).on(userGroup.userId.eq(user.id))
                .where(user.id.eq(2L))
                .fetch()

        then:
        groups.size() == 1
        groups.forEach { g ->
            assert g.id == 1
            assert g.name == "G1"
        }
    }

    def "should get all rows from an Entity based on a Join Table mapping using FK join"() {
        given:
        QGroup group = QGroup.INSTANCE
        QUser user = QUser.INSTANCE
        QUserGroup userGroup = QUserGroup.INSTANCE

        when:
        List<Group> groups = queryFactory.query()
                .select(
                        constructor(
                                Group,
                                group.id,
                                group.name
                        ))
                .from(userGroup)
                .innerJoin(userGroup.group, group)
                .innerJoin(userGroup.user, user)
                .where(user.id.eq(2L))
                .fetch()

        then:
        groups.size() == 1
        groups.forEach { g ->
            assert g.id == 1
            assert g.name == "G1"
        }
    }
}
