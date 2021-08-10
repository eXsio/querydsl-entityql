package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.it.dynamic

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLExpressions.count
import com.querydsl.sql.SQLQueryFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.entity.scanner.SpringDataJdbcQEntityScanner
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KBook
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KUploadedFile
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KUpperCaseWithUnderscoresNamingStrategy
import java.math.BigDecimal
import java.util.*
import javax.transaction.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class KQSpringDataJDBCDmlDynamicIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    var scanner = SpringDataJdbcQEntityScanner(KUpperCaseWithUnderscoresNamingStrategy())

    @Test
    fun shouldCorrectlyInsertNewEntity() {
        //given:
        val book = qEntity(KBook::class.java, scanner)

        //when:
        queryFactory!!.insert(book)
                .set(book.longNumber("id"), 10L)
                .set(book.string("name"), "newBook")
                .set(book.decimalNumber("price"), BigDecimal.ONE)
                .execute();

        //then:
        assertEquals(queryFactory!!.query().select(count()).from(book).fetchOne(), 10L)
    }

    @Test
    fun shouldCorrectlyInsertNewEntityUsingSetMethod() {
        //given:
        val book = qEntity(KBook::class.java, scanner)

        //when:
        book.set(
                queryFactory!!.insert(book),
                "id", 11L,
                "name", "newBook2",
                "price", BigDecimal.ONE)
                .execute()


        //then:
        assertEquals(queryFactory!!.query().select(count()).from(book).fetchOne(), 10L)
    }

    @Test
    fun shouldCorrectlyUpdateExistingEntity() {
        //given:
        val book = qEntity(KBook::class.java, scanner)

        //when:
        queryFactory!!.update(book)
                .set(book.string("name"), "updatedBook")
                .set(book.decimalNumber("price"), BigDecimal.ONE)
                .where(book.longNumber("id").eq(9L))
                .execute();

        //then:
        assertEquals(queryFactory!!.query().select(count()).from(book)
                .where(book.string("name").eq("updatedBook")
                        .and(book.decimalNumber("price").eq(BigDecimal.ONE))
                        .and(book.longNumber("id").eq(9L)))
                .fetchOne(), 1L)
    }

    @Test
    fun shouldCorrectlyUpdateExistingEntityUsingSetMethod() {
        //given:
        val book = qEntity(KBook::class.java, scanner)

        //when:
        val update = queryFactory!!.update(book)
                .where(book.longNumber("id").eq(9L))

        book.set(update,
                "name", "updatedBook",
                "price", BigDecimal.ONE
        ).execute()

        //then:
        assertEquals(queryFactory!!.query().select(count()).from(book)
                .where(book.string("name").eq("updatedBook")
                        .and(book.decimalNumber("price").eq(BigDecimal.ONE))
                        .and(book.longNumber("id").eq(9L)))
                .fetchOne(), 1L)
    }

    @Test
    fun shouldCorrectlyDeleteExistingEntity() {
        //given:
        val book = qEntity(KBook::class.java, scanner)

        //when:
        queryFactory!!.delete(book)
                .where(book.longNumber("id").eq(4L))
                .execute();

        //then:
        assertEquals(queryFactory!!.query().select(count()).from(book).fetchOne(), 8L)
    }

    @Test
    fun shouldInsertAndReadByteArray() {
        //given:
        val file = qEntity(KUploadedFile::class.java, scanner)

        val id = UUID.randomUUID()
        val data = "someData".toByteArray()

        //when:
        queryFactory!!.insert(file)
                .set(file.uuid("id"), id)
                .set(file.array<ByteArray, Byte>("data"), data)
                .execute()

        //and:
        val uploadedFile = queryFactory!!.select(
                constructor(KUploadedFile::class.java,
                        file.uuid("id"),
                        file.array<ByteArray, Byte>("data")
                ))
                .from(file)
                .where(file.uuid("id").eq(id))
                .fetchOne()

        //then:
        assertNotNull(uploadedFile)
        assertEquals(uploadedFile.data.size, data.size)
        assertTrue(Arrays.equals(uploadedFile.data, data))
    }

    @Test
    fun shouldThrowExceptionWhenTryingToUseOddNumberOfParametersInSet() {
        assertThrows<InvalidArgumentException> {
            //given:
            val book = qEntity(KBook::class.java, scanner)

            //when:
            val update = queryFactory!!.update(book)
                .where(book.longNumber("id").eq(9L))

            book.set(update,
                "name", "updatedBook",
                "price"
            ).execute()
        }

    }

    @Test
    fun shouldThrowExceptionWhenTryingToUseNonStringKeyInDet() {
        assertThrows<InvalidArgumentException> {
            //given:
            val book = qEntity(KBook::class.java, scanner)

            //when:
            val update = queryFactory!!.update(book)
                .where(book.longNumber("id").eq(9L))

            book.set(update,
                "name", "updatedBook",
                Object(), BigDecimal.ONE
            ).execute()
        }

    }
}
