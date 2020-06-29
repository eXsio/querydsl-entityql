package pl.exsio.querydsl.entityql.kotlin.jpa.entity

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "BOOKS")
public class KBook() {

    @Id
    @Column(name = "BOOK_ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "NAME", unique = true)
    var name: String? = null

    @Column(name = "DESC", nullable = true, columnDefinition = "CLOB")
    var desc: String? = null

    @Column(name = "PRICE")
    var price: BigDecimal? = null

    constructor(id: Long, name: String, desc: String, price: BigDecimal) : this() {
        this.id = id
        this.name = name
        this.desc = desc
        this.price = price
    }
}
