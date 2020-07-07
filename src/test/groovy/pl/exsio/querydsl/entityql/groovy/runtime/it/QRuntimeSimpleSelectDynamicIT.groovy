package pl.exsio.querydsl.entityql.groovy.runtime.it

import com.querydsl.core.Tuple
import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.groovy.config.SpringContext
import pl.exsio.querydsl.entityql.groovy.config.enums.by_name.UserTypeByName
import pl.exsio.querydsl.entityql.groovy.config.enums.by_ordinal.UserTypeByOrdinal
import pl.exsio.querydsl.entityql.type.SimpleMapProjections

import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QRuntimeSimpleSelectDynamicIT extends RuntimeFixedTestBase {

    @Autowired
    SQLQueryFactory queryFactory

    def "should get all rows from an Entity"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)

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
                .fetch()

        then:
        books.size() == 9
        books.forEach { p ->
            assert p.bookId != null
            assert p.name != null
            assert p.desc != null
            assert p.price != null
        }
    }

    def "should get one row from an Entity"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)

        when:
        Map<String, ?> p = queryFactory.query()
                .select(
                        SimpleMapProjections.map(
                                book.longNumber("bookId"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .where(book.longNumber("bookId").eq(1L))
                .from(book)
                .fetchOne()

        then:
        p != null
        p.bookId != null
        p.price != null
        p.desc != null
        p.name != null
    }

    def "should get all rows from an Entity based on an Enum String filter"() {
        given:
        Q<Tuple> user = qEntity(TABLE_USERS)

        when:
        String userName = queryFactory.query()
                .select(user.string("name"))
                .where(user.<UserTypeByName> enumerated("typeStr").eq(UserTypeByName.ADMIN))
                .from(user)
                .fetchOne()

        then:
        userName == "U1"
    }

    def "should get all rows from an Entity based on an Enum Ordinal filter"() {
        given:
        Q<Tuple> user = qEntity(TABLE_USERS)

        when:
        String userName = queryFactory.query()
                .select(user.string("name"))
                .where(user.<UserTypeByOrdinal>enumerated("typeOrd").eq(UserTypeByOrdinal.ADMIN))
                .from(user)
                .fetchOne()

        then:
        userName == "U1"
    }

    def "should get generic Fields"() {
        given:
        Q<Tuple> user = qEntity(TABLE_USERS)

        when:
        String createdBy = queryFactory.query()
                .select(user.<String> column("createdBy"))
                .where(user.<UserTypeByName> enumerated("typeStr").eq(UserTypeByName.ADMIN))
                .from(user)
                .fetchOne()

        then:
        createdBy == "ADMIN"
    }

    def "should get unknown Fields"() {
        given:
        Q<Tuple> user = qEntity(TABLE_USERS)

        when:
        Date createdBy = queryFactory.query()
                .select(user.<Date> column("createdAt"))
                .where(user.<UserTypeByName> enumerated("typeStr").eq(UserTypeByName.ADMIN))
                .from(user)
                .fetchOne()

        then:
        createdBy != null
    }

    def "should get enum Fields"() {
        given:
        Q<Tuple> user = qEntity(TABLE_USERS)

        when:
        UserTypeByName type = queryFactory.query()
                .select(user.<UserTypeByName>enumerated("typeStr"))
                .where(user.<UserTypeByName>enumerated("typeStr").eq(UserTypeByName.ADMIN))
                .from(user)
                .fetchOne()

        then:
        type == UserTypeByName.ADMIN
    }

    def "should get boolean Fields"() {
        given:
        Q<Tuple> user = qEntity(TABLE_USERS)

        when:
        Boolean enabled = queryFactory.query()
                .select(user.bool("enabled"))
                .where(user.<UserTypeByName>enumerated("typeStr").eq(UserTypeByName.ADMIN))
                .from(user)
                .fetchOne()

        then:
        enabled
    }

    def "should get enum and boolean Fields in DTO projection"() {
        given:
        Q<Tuple> user = qEntity(TABLE_USERS)

        when:
        Map<String, ?> userDto = queryFactory.query()
                .select(
                    SimpleMapProjections.map(
                            user.longNumber("userId"),
                            user.string("name"),
                            user.<UserTypeByName>enumerated("typeStr"),
                            user.bool("enabled")
                    ))
                .where(user.<UserTypeByName>enumerated("typeStr").eq(UserTypeByName.ADMIN))
                .from(user)
                .fetchOne()

        then:
        userDto != null
        userDto.enabled
        userDto.typeStr == UserTypeByName.ADMIN
    }

}
