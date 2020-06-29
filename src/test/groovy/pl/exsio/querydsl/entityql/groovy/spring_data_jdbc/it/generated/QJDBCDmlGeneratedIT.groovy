package pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.it.generated

import com.querydsl.sql.SQLQueryFactory
import com.querydsl.sql.dml.SQLUpdateClause
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.groovy.config.SpringContext
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException
import pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it.UploadedFile
import pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it.generated.QBook
import pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it.generated.QUploadedFile
import spock.lang.Specification

import javax.transaction.Transactional
import java.util.stream.IntStream

import static com.querydsl.core.types.Projections.constructor
import static com.querydsl.sql.SQLExpressions.count

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class QJDBCDmlGeneratedIT extends Specification {

    @Autowired
    SQLQueryFactory queryFactory

    def "should correctly insert new Entity"() {
        given:
        QBook book = QBook.INSTANCE

        when:
        queryFactory.insert(book)
                .set(book.id, 10L)
                .set(book.name, "newBook")
                .set(book.price, BigDecimal.ONE)
                .execute();

        then:
        queryFactory.query().select(count()).from(book).fetchOne() == 10L
    }

    def "should correctly insert new Entity using set() method"() {
        given:
        QBook book = QBook.INSTANCE

        when:
        book.set(
                queryFactory.insert(book),
                book.id, 11L,
                book.name, "newBook2",
                book.price, BigDecimal.ONE)
                .execute()


        then:
        queryFactory.query().select(count()).from(book).fetchOne() == 10L
    }

    def "should correctly update existing Entity"() {
        given:
        QBook book = QBook.INSTANCE

        when:
        queryFactory.update(book)
                .set(book.name, "updatedBook")
                .set(book.price, BigDecimal.ONE)
                .where(book.id.eq(9L))
                .execute();

        then:
        queryFactory.query().select(count()).from(book)
                .where(book.name.eq("updatedBook")
                        .and(book.price.eq(BigDecimal.ONE))
                        .and(book.id.eq(9L)))
                .fetchOne() == 1L
    }


    def "should correctly update existing Entity using set() method"() {
        given:
        QBook book = QBook.INSTANCE

        when:
        SQLUpdateClause update = queryFactory.update(book)
                .where(book.id.eq(9L))

        book.set(update,
                book.name, "updatedBook",
                book.price, BigDecimal.ONE
        ).execute()

        then:
        queryFactory.query().select(count()).from(book)
                .where(book.name.eq("updatedBook")
                        .and(book.price.eq(BigDecimal.ONE))
                        .and(book.id.eq(9L)))
                .fetchOne() == 1L
    }

    def "should correctly delete existing Entity"() {
        given:
        QBook book = QBook.INSTANCE

        when:
        queryFactory.delete(book)
                .where(book.id.eq(4L))
                .execute();

        then:
        queryFactory.query().select(count()).from(book).fetchOne() == 8L
    }

    def "should insert and read byte array"() {
        given:
        QUploadedFile file = QUploadedFile.INSTANCE

        UUID id = UUID.randomUUID()
        byte[] data = "someData".getBytes()

        when:
        queryFactory.insert(file)
                .set(file.id, id)
                .set(file.data, data)
                .execute()

        and:
        UploadedFile uploadedFile = queryFactory.select(
                constructor(UploadedFile, file.data, file.id))
                .from(file)
                .where(file.id.eq(id))
                .fetchOne()

        then:
        noExceptionThrown()

        and:
        uploadedFile != null
        uploadedFile.id != null
        uploadedFile.data != null
        uploadedFile.data.length == data.length
        Arrays.equals(uploadedFile.data, data)
    }

    def "should throw exception when trying to use odd number of parameters in set()"() {
        given:
        QBook book = QBook.INSTANCE

        when:
        SQLUpdateClause update = queryFactory.update(book)
                .where(book.id.eq(9L))

        book.set(update,
                book.name, "updatedBook",
                book.price
        ).execute()

        then:
        thrown InvalidArgumentException
    }

    def "should throw exception when trying to use non-String key in set()"() {
        given:
        QBook book = QBook.INSTANCE

        when:
        SQLUpdateClause update = queryFactory.update(book)
                .where(book.id.eq(9L))

        book.set(update,
                book.name, "updatedBook",
                new Object(), BigDecimal.ONE
        ).execute()

        then:
        thrown InvalidArgumentException
    }
}
