package pl.exsio.querydsl.entityql.groovy.jpa.it.generated

import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.groovy.config.SpringContext
import pl.exsio.querydsl.entityql.groovy.config.dto.UserDto
import pl.exsio.querydsl.entityql.groovy.config.enums.by_name.UserTypeByName
import pl.exsio.querydsl.entityql.groovy.config.enums.by_ordinal.UserTypeByOrdinal
import pl.exsio.querydsl.entityql.groovy.jpa.entity.it.Book
import pl.exsio.querydsl.entityql.groovy.jpa.entity.it.generated.QBook
import pl.exsio.querydsl.entityql.groovy.jpa.entity.it.generated.QUser
import spock.lang.Specification

import static com.querydsl.core.types.Projections.constructor

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QJPASimpleSelectGeneratedIT extends Specification {

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

    def "should get all rows from an Entity based on an Enum String filter"() {
        given:
        QUser user = QUser.INSTANCE

        when:
        String userName = queryFactory.query()
                .select(user.name)
                .where(user.typeStr.eq(UserTypeByName.ADMIN))
                .from(user).fetchOne()

        then:
        userName == "U1"
    }

    def "should get all rows from an Entity based on an Enum Ordinal filter"() {
        given:
        QUser user = QUser.INSTANCE

        when:
        String userName = queryFactory.query()
                .select(user.name)
                .where(user.typeOrd.eq(UserTypeByOrdinal.ADMIN))
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
                .where(user.typeStr.eq(UserTypeByName.ADMIN))
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
                .where(user.typeStr.eq(UserTypeByName.ADMIN))
                .from(user).fetchOne()

        then:
        createdBy != null
    }

    def "should get enum Fields"() {
        given:
        QUser user = QUser.INSTANCE

        when:
        UserTypeByName type = queryFactory.query()
                .select(user.typeStr)
                .where(user.typeStr.eq(UserTypeByName.ADMIN))
                .from(user).fetchOne()

        then:
        type == UserTypeByName.ADMIN
    }

    def "should get boolean Fields"() {
        given:
        QUser user = QUser.INSTANCE

        when:
        Boolean enabled = queryFactory.query()
                .select(user.enabled)
                .where(user.typeStr.eq(UserTypeByName.ADMIN))
                .from(user).fetchOne()

        then:
        enabled
    }

    def "should get enum and boolean Fields in DTO projection"() {
        given:
        QUser user = QUser.INSTANCE

        when:
        UserDto userDto = queryFactory.query()
                .select(constructor(UserDto, user.id, user.name, user.typeStr, user.enabled))
                .where(user.typeStr.eq(UserTypeByName.ADMIN))
                .from(user).fetchOne()

        then:
        userDto != null
        userDto.enabled
        userDto.type == UserTypeByName.ADMIN
    }
}
