package pl.exsio.querydsl.entityql.spring_data_jdbc.it.dynamic

import com.querydsl.sql.SQLQueryFactory
import com.querydsl.sql.dml.SQLUpdateClause
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.config.SpringContext
import pl.exsio.querydsl.entityql.entity.scanner.SpringDataJdbcQEntityScanner
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.UpperCaseWithUnderscoresNamingStrategy
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it.Book
import pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it.UploadedFile
import spock.lang.Specification

import javax.transaction.Transactional
import java.util.stream.IntStream

import static com.querydsl.core.types.Projections.constructor
import static com.querydsl.sql.SQLExpressions.count
import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class QJDBCDmlDynamicIT extends Specification {

    @Autowired
    SQLQueryFactory queryFactory

    SpringDataJdbcQEntityScanner scanner = new SpringDataJdbcQEntityScanner(new UpperCaseWithUnderscoresNamingStrategy());

    def "should correctly insert new Entity"() {
        given:
        Q<Book> book = qEntity(Book, scanner)

        when:
        queryFactory.insert(book)
                .set(book.longNumber("id"), 10L)
                .set(book.string("name"), "newBook")
                .set(book.decimalNumber("price"), BigDecimal.ONE)
                .execute();

        then:
        queryFactory.query().select(count()).from(book).fetchOne() == 10L
    }

    def "should correctly insert new Entity using set() method"() {
        given:
        Q<Book> book = qEntity(Book, scanner)

        when:
        book.set(
                queryFactory.insert(book),
                "id", 11L,
                "name", "newBook2",
                "price", BigDecimal.ONE)
                .execute()


        then:
        queryFactory.query().select(count()).from(book).fetchOne() == 10L
    }

    def "should correctly update existing Entity"() {
        given:
        Q<Book> book = qEntity(Book, scanner)

        when:
        queryFactory.update(book)
                .set(book.string("name"), "updatedBook")
                .set(book.decimalNumber("price"), BigDecimal.ONE)
                .where(book.longNumber("id").eq(9L))
                .execute();

        then:
        queryFactory.query().select(count()).from(book)
                .where(book.string("name").eq("updatedBook")
                        .and(book.decimalNumber("price").eq(BigDecimal.ONE))
                        .and(book.longNumber("id").eq(9L)))
                .fetchOne() == 1L
    }


    def "should correctly update existing Entity using set() method"() {
        given:
        Q<Book> book = qEntity(Book, scanner)

        when:
        SQLUpdateClause update = queryFactory.update(book)
                .where(book.longNumber("id").eq(9L))

        book.set(update,
                "name", "updatedBook",
                "price", BigDecimal.ONE
        ).execute()

        then:
        queryFactory.query().select(count()).from(book)
                .where(book.string("name").eq("updatedBook")
                        .and(book.decimalNumber("price").eq(BigDecimal.ONE))
                        .and(book.longNumber("id").eq(9L)))
                .fetchOne() == 1L
    }

    def "should correctly delete existing Entity"() {
        given:
        Q<Book> book = qEntity(Book, scanner)

        when:
        queryFactory.delete(book)
                .where(book.longNumber("id").eq(4L))
                .execute();

        then:
        queryFactory.query().select(count()).from(book).fetchOne() == 8L
    }

    def "should insert and read byte array"() {
        given:
        Q<UploadedFile> file = qEntity(UploadedFile, scanner)

        UUID id = UUID.randomUUID()
        int size = 10
        byte[] data = new byte[size]
        IntStream.range(0, size).forEach { i -> data[i] = 2 }

        when:
        queryFactory.insert(file)
                .set(file.uuid("id"), id)
                .set(file.<byte[], Byte>array("data"), data)
                .execute()

        and:
        UploadedFile uploadedFile = queryFactory.select(
                constructor(UploadedFile, file.<byte[], Byte>array("data"), file.uuid("id")))
                .from(file)
                .where(file.uuid("id").eq(id))
                .fetchOne()

        then:
        noExceptionThrown()

        and:
        uploadedFile != null
        uploadedFile.id != null
        uploadedFile.data != null
        uploadedFile.data.size() == size
        Arrays.equals(uploadedFile.data, data)
    }

    def "should throw exception when trying to use odd number of parameters in set()"() {
        given:
        Q<Book> book = qEntity(Book, scanner)

        when:
        SQLUpdateClause update = queryFactory.update(book)
                .where(book.longNumber("id").eq(9L))

        book.set(update,
                "name", "updatedBook",
                "price"
        ).execute()

        then:
        thrown InvalidArgumentException
    }

    def "should throw exception when trying to use non-String key in set()"() {
        given:
        Q<Book> book = qEntity(Book, scanner)

        when:
        SQLUpdateClause update = queryFactory.update(book)
                .where(book.longNumber("id").eq(9L))

        book.set(update,
                "name", "updatedBook",
                new Object(), BigDecimal.ONE
        ).execute()

        then:
        thrown InvalidArgumentException
    }
}
