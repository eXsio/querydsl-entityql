package pl.exsio.querydsl.entityql.it.generated

import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.config.SpringContext
import pl.exsio.querydsl.entityql.config.entity.it.Book
import pl.exsio.querydsl.entityql.config.entity.it.User
import pl.exsio.querydsl.entityql.config.entity.it.generated.QBook
import pl.exsio.querydsl.entityql.config.entity.it.generated.QUser
import spock.lang.Specification

import static com.querydsl.core.types.Projections.constructor
import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QSimpleSelectGeneratedIT extends Specification {

    @Autowired
    SQLQueryFactory queryFactory

    def "should get all rows from an Entity"() {
        given:
        QBook book = QBook.INSTANCE

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
                .from(book).fetch()

        then:
        books.size() == 9
        books.forEach { p ->
            assert p.id != null
            assert p.name != null
            assert p.desc != null
            assert p.price != null
        }
    }

    def "should get one row from an Entity"() {
        given:
        QBook book = QBook.INSTANCE

        when:
        Book p = queryFactory.query()
                .select(
                        constructor(
                                Book,
                                book.id,
                                book.name,
                                book.desc,
                                book.price
                        ))
                .where(book.id.eq(1L))
                .from(book).fetchOne()

        then:
        p != null
        p.id != null
        p.price != null
        p.desc != null
        p.name != null
    }

    def "should get all rows from an Entity based on an Enum filter"() {
        given:
        QUser user = QUser.INSTANCE

        when:
        String userName = queryFactory.query()
                .select(user.name)
                .where(user.typeStr.eq(User.Type.ADMIN))
                .from(user).fetchOne()

        then:
        userName == "U1"
    }

    def "should get generic Fields"() {
        given:
        QUser user = QUser.INSTANCE

        when:
        String createdBy = queryFactory.query()
                .select(user.createdBy)
                .where(user.typeStr.eq(User.Type.ADMIN))
                .from(user).fetchOne()

        then:
        createdBy == "ADMIN"
    }

    def "should get unknown Fields"() {
        given:
        QUser user = QUser.INSTANCE

        when:
        Date createdBy = queryFactory.query()
                .select(user.createdAt)
                .where(user.typeStr.eq(User.Type.ADMIN))
                .from(user).fetchOne()

        then:
        createdBy != null
    }
}
