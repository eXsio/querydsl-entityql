package pl.exsio.querydsl.entityql.kotlin.jpa.entity


import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "ORDERS")
class KOrder() : Serializable {

    @Id
    @Column(name = "ORDER_ID")
    @GeneratedValue
    var id: Long? = null

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    var user: KUser<Any>? = null

    @OneToMany(mappedBy = "order")
    var items: List<KOrderItem>? = null

    @OneToMany(mappedBy = "orderReferenced")
    var itemsReferenced: List<KOrderItem>? = null


}
