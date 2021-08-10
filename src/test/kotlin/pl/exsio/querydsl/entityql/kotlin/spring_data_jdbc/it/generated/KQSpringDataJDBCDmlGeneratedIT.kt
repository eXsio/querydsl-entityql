package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.it.generated

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLExpressions.count
import com.querydsl.sql.SQLQueryFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.KUploadedFile
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated.QKBook
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated.QKUploadedFile
import java.math.BigDecimal
import java.util.*
import javax.transaction.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class KQSpringDataJDBCDmlGeneratedIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldCorrectlyInsertNewEntity() {
        //given:
        val book = QKBook.instance

        //when:
        queryFactory!!.insert(book)
            .set(book.id, 10L)
            .set(book.name, "newBook")
            .set(book.price, BigDecimal.ONE)
            .execute();

        //then:
        assertEquals(queryFactory!!.query().select(count()).from(book).fetchOne(), 10L)
    }

    @Test
    fun shouldCorrectlyInsertNewEntityUsingSetMethod() {
        //given:
        val book = QKBook.instance

        //when:
        book.set(
            queryFactory!!.insert(book),
            book.id, 11L,
            book.name, "newBook2",
            book.price, BigDecimal.ONE
        )
            .execute()


        //then:
        assertEquals(queryFactory!!.query().select(count()).from(book).fetchOne(), 10L)
    }

    @Test
    fun shouldCorrectlyUpdateExistingEntity() {
        //given:
        val book = QKBook.instance

        //when:
        queryFactory!!.update(book)
            .set(book.name, "updatedBook")
            .set(book.price, BigDecimal.ONE)
            .where(book.id.eq(9L))
            .execute();

        //then:
        assertEquals(
            queryFactory!!.query().select(count()).from(book)
                .where(
                    book.name.eq("updatedBook")
                        .and(book.price.eq(BigDecimal.ONE))
                        .and(book.id.eq(9L))
                )
                .fetchOne(), 1L
        )
    }

    @Test
    fun shouldCorrectlyUpdateExistingEntityUsingSetMethod() {
        //given:
        val book = QKBook.instance

        //when:
        val update = queryFactory!!.update(book)
            .where(book.longNumber("id").eq(9L))

        book.set(
            update,
            book.name, "updatedBook",
            book.price, BigDecimal.ONE
        ).execute()

        //then:
        assertEquals(
            queryFactory!!.query().select(count()).from(book)
                .where(
                    book.name.eq("updatedBook")
                        .and(book.price.eq(BigDecimal.ONE))
                        .and(book.id.eq(9L))
                )
                .fetchOne(), 1L
        )
    }

    @Test
    fun shouldCorrectlyDeleteExistingEntity() {
        //given:
        val book = QKBook.instance

        //when:
        queryFactory!!.delete(book)
            .where(book.id.eq(4L))
            .execute();

        //then:
        assertEquals(queryFactory!!.query().select(count()).from(book).fetchOne(), 8L)
    }

    @Test
    fun shouldInsertAndReadByteArray() {
        //given:
        val file = QKUploadedFile.instance

        val id = UUID.randomUUID()
        val data = "someData".toByteArray()

        //when:
        queryFactory!!.insert(file)
            .set(file.id, id)
            .set(file.data, data)
            .execute()

        //and:
        val uploadedFile = queryFactory!!.select(
            constructor(
                KUploadedFile::class.java,
                file.id,
                file.data
            )
        )
            .from(file)
            .where(file.id.eq(id))
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
            val book = QKBook.instance

            //when:
            val update = queryFactory!!.update(book)
                .where(book.id.eq(9L))

            book.set(
                update,
                book.name, "updatedBook",
                book.price
            ).execute()
        }

    }
}
