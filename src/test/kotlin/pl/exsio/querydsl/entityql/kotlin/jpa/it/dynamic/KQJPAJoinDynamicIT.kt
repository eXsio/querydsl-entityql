package pl.exsio.querydsl.entityql.kotlin.jpa.it.dynamic

import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQueryFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import pl.exsio.querydsl.entityql.EntityQL.dto
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.kotlin.config.KSpringContext
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.*
import kotlin.test.assertNotNull

@ContextConfiguration(classes = [KSpringContext::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class KQJPAJoinDynamicIT {

    @Autowired
    var queryFactory: SQLQueryFactory? = null

    @Test
    fun shouldGetAllRowsFromAnEntityBasedOnAColumnONJoin() {
        //given:
        val book = qEntity(KBook::class.java)
        val order = qEntity(KOrder::class.java)
        val orderItem = qEntity(KOrderItem::class.java)

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(book)
                .innerJoin(orderItem).on(orderItem.longNumber("bookId").eq(book.longNumber("id")))
                .innerJoin(order).on(orderItem.longNumber("orderId").eq(order.longNumber("id")))
                .where(order.longNumber("id").eq(1L))
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
        val book = qEntity(KBook::class.java)
        val order = qEntity(KOrder::class.java)
        val orderItem = qEntity(KOrderItem::class.java)

        //when:
        val books = queryFactory!!.query()
                .select(
                        dto(KBook::class.java, book.columns("id", "name", "desc", "price"))
                )
                .from(book)
                .innerJoin(orderItem).on(orderItem.longNumber("bookId").eq(book.longNumber("id")))
                .innerJoin(order).on(orderItem.longNumber("orderId").eq(order.longNumber("id")))
                .where(order.longNumber("id").eq(1L))
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
        val book = qEntity(KBook::class.java)
        val order = qEntity(KOrder::class.java)
        val orderItem = qEntity(KOrderItem::class.java)

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(orderItem)
                .innerJoin(orderItem.joinColumn<KBook>("book"), book)
                .innerJoin(orderItem.joinColumn<KOrder>("order"), order)
                .where(order.longNumber("id").eq(2L))
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
        val group = qEntity(KGroup::class.java)
        val groupAdmin = qEntity(KGroupAdmin::class.java)

        //when:
        val groups = queryFactory!!.query()
                .select(
                        constructor(
                                KGroup::class.java,
                                group.longNumber("id"),
                                group.string("name")
                        ))
                .from(group)
                .innerJoin(group.joinColumn<KGroupAdmin>("admin"), groupAdmin)
                .where(groupAdmin.longNumber("id").eq(2L))
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
        val group = qEntity(KGroup::class.java)
        val user = qEntity(KUser::class.java)
        val userGroup = qEntity(KUserGroup::class.java)

        //when:
        val groups = queryFactory!!.query()
                .select(
                        constructor(
                                KGroup::class.java,
                                group.longNumber("id"),
                                group.string("name")
                        ))
                .from(userGroup)
                .innerJoin(group).on(userGroup.longNumber("groupId").eq(group.longNumber("id")))
                .innerJoin(user).on(userGroup.longNumber("userId").eq(user.longNumber("id")))
                .where(user.longNumber("id").eq(2L))
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
        val group = qEntity(KGroup::class.java)
        val user = qEntity(KUser::class.java)
        val userGroup = qEntity(KUserGroup::class.java)

        //when:
        val groups = queryFactory!!.query()
                .select(
                        constructor(
                                KGroup::class.java,
                                group.longNumber("id"),
                                group.string("name")
                        ))
                .from(userGroup)
                .innerJoin(userGroup.joinColumn<KGroup>("group"), group)
                .innerJoin(userGroup.joinColumn<KUser<*>>("user"), user)
                .where(user.longNumber("id").eq(2L))
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
        val book = qEntity(KBook::class.java)
        val order = qEntity(KOrder::class.java)
        val orderItem = qEntity(KOrderItem::class.java)

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(order)
                .innerJoin(order.inverseJoinColumn<KOrderItem>("items"), orderItem)
                .innerJoin(orderItem.joinColumn<KBook>("book"), book)
                .where(order.longNumber("id").eq(2L))
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

        val book = qEntity(KBook::class.java)
        val order = qEntity(KOrder::class.java)
        val orderItem = qEntity(KOrderItem::class.java)

        //when:
        val books = queryFactory!!.query()
                .select(
                        constructor(
                                KBook::class.java,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(order)
                .innerJoin(order.inverseJoinColumn<KOrderItem>("itemsReferenced"), orderItem)
                .innerJoin(orderItem.joinColumn<KBook>("book"), book)
                .where(order.longNumber("id").eq(2L))
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
