package pl.exsio.querydsl.entityql.kotlin.jpa.it.dynamic

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLExpressions.count
import com.querydsl.sql.SQLQueryFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KBook
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KUploadedFile
import java.math.BigDecimal
import java.util.*
import javax.transaction.Transactional
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class KQJPADmlDynamicIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldCorrectlyInsertNewEntity() {
        //given:
        val book = qEntity(KBook::class.java)

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
        val book = qEntity(KBook::class.java)

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
        val book = qEntity(KBook::class.java)

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
        val book = qEntity(KBook::class.java)

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
        val book = qEntity(KBook::class.java)

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
        val file = qEntity(KUploadedFile::class.java)

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
        uploadedFile != null
        uploadedFile.id != null
        uploadedFile.data != null
        uploadedFile.data!!.size == data.size
        Arrays.equals(uploadedFile.data, data)
    }

    @Test(expected = InvalidArgumentException::class)
    fun shouldThrowExceptionWhenTryingToUseOddNumberOfParametersInSet() {
        //given:
        val book = qEntity(KBook::class.java)

        //when:
        val update = queryFactory!!.update(book)
                .where(book.longNumber("id").eq(9L))

        book.set(update,
                "name", "updatedBook",
                "price"
        ).execute()
    }

    @Test(expected = InvalidArgumentException::class)
    fun shouldThrowExceptionWhenTryingToUseNonStringKeyInDet() {
        //given:
        val book = qEntity(KBook::class.java)

        //when:
        val update = queryFactory!!.update(book)
                .where(book.longNumber("id").eq(9L))

        book.set(update,
                "name", "updatedBook",
                Object(), BigDecimal.ONE
        ).execute()
    }
}
