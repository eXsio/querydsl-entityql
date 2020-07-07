package pl.exsio.querydsl.entityql.groovy.runtime.it

import com.querydsl.core.Tuple
import com.querydsl.core.types.Projections
import com.querydsl.sql.SQLQueryFactory
import com.querydsl.sql.dml.SQLUpdateClause
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException
import pl.exsio.querydsl.entityql.groovy.config.SpringContext

import javax.transaction.Transactional
import java.nio.charset.StandardCharsets

import static com.querydsl.sql.SQLExpressions.count
import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class QRuntimeDmlDynamicIT extends RuntimeFixedTestBase {

    @Autowired
    SQLQueryFactory queryFactory

    def "should correctly insert new Entity"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)
        def bookId = book.longNumber("bookId")

        when:
        queryFactory.insert(book)
                .set(bookId, 19L)
                .set(book.string("name"), "oldBook")
                .set(book.decimalNumber("price"), BigDecimal.ONE.add(BigDecimal.ONE))
                .execute()

        and:
        def actualResult = queryFactory.query()
                .select(count())
                .from(book)
                .where(bookId.eq(19L))
                .fetchOne()

        then:
        actualResult == 1L
    }

    def "should correctly insert new Entity using set() method"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)
        def bookId = book.longNumber("bookId")

        when:
        book.set(
                queryFactory.insert(book),
                "bookId", 20L,
                "name", "newBook2",
                "price", BigDecimal.ONE)
                .execute()

        and:
        def actualResult = queryFactory.query()
                .select(count())
                .from(book)
                .where(bookId.eq(20L))
                .fetchOne()

        then:
        actualResult == 1L
    }

    def "should correctly update existing Entity"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)

        when:
        queryFactory.update(book)
                .set(book.string("name"), "updatedBook")
                .set(book.decimalNumber("price"), BigDecimal.ONE)
                .where(book.longNumber("bookId").eq(9L))
                .execute()

        and:
        def actualResult = queryFactory.query()
                .select(count())
                .from(book)
                .where(book.string("name").eq("updatedBook")
                .and(book.decimalNumber("price").eq(BigDecimal.ONE))
                .and(book.longNumber("bookId").eq(9L)))
                .fetchOne()

        then:
        actualResult == 1L
    }


    def "should correctly update existing Entity using set() method"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)

        when:
        SQLUpdateClause update = queryFactory.update(book)
                .where(book.longNumber("bookId").eq(9L))

        book.set(update,
                "name", "updatedBook",
                "price", BigDecimal.TEN
        ).execute()

        and:
        def actualResult = queryFactory.query().select(count()).from(book)
                .where(book.string("name").eq("updatedBook")
                .and(book.decimalNumber("price").eq(BigDecimal.TEN))
                .and(book.longNumber("bookId").eq(9L)))
                .fetchOne()

        then:
        actualResult == 1L
    }

    def "should correctly delete existing Entity"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)

        when:
        queryFactory.delete(book)
                .where(book.longNumber("bookId").eq(4L))
                .execute()

        then:
        queryFactory.query().select(count()).from(book).fetchOne() == 8L
    }

    def "should insert and read byte array"() {
        given:
        Q<Tuple> file = qEntity(TABLE_UPLOADED_FILES)
        def fileId = file.uuid("fileId")
        def data = file.<byte[], Byte> array("data")

        UUID id = UUID.randomUUID()
        def text = "Hello world"
        byte[] dataArray = text.getBytes(StandardCharsets.UTF_8)

        when:
        queryFactory.insert(file)
                .set(fileId, id)
                .set(data, dataArray)
                .execute()

        and:
        def (byte[] actualData, UUID actualId) = queryFactory.select(
                Projections.list(data, fileId))
                .from(file)
                .where(fileId.eq(id))
                .fetchOne()

        then:
        noExceptionThrown()

        and:
        actualId != null
        actualData != null
        actualData.size() == text.length()
        actualData == dataArray
    }

    def "should throw exception when trying to use odd number of parameters in set()"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)

        when:
        SQLUpdateClause update = queryFactory.update(book)
                .where(book.longNumber("bookId").eq(9L))

        book.set(update,
                "name", "updatedBook",
                "price"
        ).execute()

        then:
        thrown InvalidArgumentException
    }

    def "should throw exception when trying to use non-String key in set()"() {
        given:
        Q<Tuple> book = qEntity(TABLE_BOOKS)

        when:
        SQLUpdateClause update = queryFactory.update(book)
                .where(book.longNumber("bookId").eq(9L))

        book.set(update,
                "name", "updatedBook",
                new Object(), BigDecimal.ONE
        ).execute()

        then:
        thrown InvalidArgumentException
    }

}
