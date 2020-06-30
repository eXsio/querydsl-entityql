package pl.exsio.querydsl.entityql.kotlin.jpa.it.generated

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQueryFactory
import kotlin.test.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.EntityQL.dto
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.*
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.generated.*
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQJPAJoinGeneratedIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAColumnONJoin() {
        //given:
        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
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

        //then:
        assertEquals(books.size, 3)
        books.forEach { b ->
            assertNotNull(b.id)
            assertNotNull(b.name)
            assertNotNull(b.desc)
            assertNotNull(b.price)
        }
    }

    @Test
    fun shouldGetAllRowsUsingListOfColumnNames() {
        //given:
        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val books = queryFactory!!.query()
                .select(
                        dto(KBook::class.java, listOf(book.id, book.name, book.desc, book.price))
                )
                .from(book)
                .innerJoin(orderItem).on(orderItem.bookId.eq(book.id))
                .innerJoin(order).on(orderItem.orderId.eq(order.id))
                .where(order.id.eq(1L))
                .fetch()

        //then:
        assertEquals(books.size, 3)
        books.forEach { b ->
            assertNotNull(b.id)
            assertNotNull(b.name)
            assertNotNull(b.desc)
            assertNotNull(b.price)
        }
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnFKJoin() {
        //given:
        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
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

        //then:
        assertEquals(books.size, 5)
        books.forEach { b ->
            assertNotNull(b.id)
            assertNotNull(b.name)
            assertNotNull(b.desc)
            assertNotNull(b.price)
        }
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnFKJoinWithCustomReferencedColumnName() {
        //given:
        val group = QKGroup.instance
        val groupAdmin = QKGroupAdmin.instance

        //when:
        val groups = queryFactory!!.query()
                .select(
                        constructor(
                                KGroup::class.java,
                                group.id,
                                group.name
                        ))
                .from(group)
                .innerJoin(group.admin, groupAdmin)
                .where(groupAdmin.id.eq(2L))
                .fetch()

        //then:
        assertEquals(groups.size, 1)
        groups.forEach { g ->
            assertEquals(g.id, 2L)
            assertEquals(g.name, "G2")
        }
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnJoinTableMappingUsingONClause() {
        //given:
        val group = QKGroup.instance
        val user = QKUser.instance
        val userGroup = QKUserGroup.instance

        //when:
        val groups = queryFactory!!.query()
                .select(
                        constructor(
                                KGroup::class.java,
                                group.id,
                                group.name
                        ))
                .from(userGroup)
                .innerJoin(group).on(userGroup.groupId.eq(group.id))
                .innerJoin(user).on(userGroup.userId.eq(user.id))
                .where(user.id.eq(2L))
                .fetch()

        //then:
        assertEquals(groups.size, 1)
        groups.forEach { g ->
            assertEquals(g.id, 1L)
            assertEquals(g.name, "G1")
        }
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnJoinTableMappingUsingFKJoin() {
        //given:
        val group = QKGroup.instance
        val user = QKUser.instance
        val userGroup = QKUserGroup.instance

        //when:
        val groups = queryFactory!!.query()
                .select(
                        constructor(
                                KGroup::class.java,
                                group.id,
                                group.name
                        ))
                .from(userGroup)
                .innerJoin(userGroup.group, group)
                .innerJoin(userGroup.user, user)
                .where(user.id.eq(2L))
                .fetch()

        //then:
        assertEquals(groups.size, 1)
        groups.forEach { g ->
            assertEquals(g.id, 1L)
            assertEquals(g.name, "G1")
        }
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAnInverseFKJoin() {
        //given:
        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.id,
                                book.name,
                                book.desc,
                                book.price
                        ))
                .from(order)
                .innerJoin(order.items, orderItem)
                .innerJoin(orderItem.book, book)
                .where(order.id.eq(2L))
                .fetch()

        //then:
        assertEquals(books.size, 5)
        books.forEach { b ->
            assertNotNull(b.id)
            assertNotNull(b.name)
            assertNotNull(b.desc)
            assertNotNull(b.price)
        }
    }

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAnInverseFKJoinAndReferencedColumnName() {

        val book = QKBook.instance
        val order = QKOrder.instance
        val orderItem = QKOrderItem.instance

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.id,
                                book.name,
                                book.desc,
                                book.price
                        ))
                .from(order)
                .innerJoin(order.itemsReferenced, orderItem)
                .innerJoin(orderItem.book, book)
                .where(order.id.eq(2L))
                .fetch()

        //then:
        assertEquals(books.size, 5)
        books.forEach { b ->
            assertNotNull(b.id)
            assertNotNull(b.name)
            assertNotNull(b.desc)
            assertNotNull(b.price)
        }
    }

}
