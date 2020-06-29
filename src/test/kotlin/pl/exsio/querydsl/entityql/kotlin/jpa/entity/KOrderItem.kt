package pl.exsio.querydsl.entityql.kotlin.jpa.entity


import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KBook
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KOrder
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "ORDER_ITEMS", uniqueConstraints = [
    UniqueConstraint(name = "junique_book_for_order",
            columnNames = ["BOOK_ID", "ITEM_ORDER_ID"])
])
class KOrderItem() : Serializable {

    @Id
    @Column(name = "ORDER_ITEM_ID")
    @GeneratedValue
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    var book: KBook? = null

    @ManyToOne
    @JoinColumn(name = "ITEM_ORDER_ID", nullable = false)
    var order: KOrder? = null

    @ManyToOne
    @JoinColumn(name = "ITEM_ORDER_ID", nullable = false, insertable = false, updatable = false, referencedColumnName = "ORDER_ID")
    var orderReferenced: KOrder? = null

    @Column(name = "QTY", nullable = false)
    var quantity: Long? = null

    constructor(id: Long, quantity: Long) : this() {
        this.id = id
        this.quantity = quantity
    }
}
