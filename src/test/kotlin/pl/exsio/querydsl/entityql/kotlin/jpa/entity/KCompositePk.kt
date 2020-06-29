package pl.exsio.querydsl.entityql.kotlin.jpa.entity

import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KCompositeFk
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "COMPOSITE_PK")
class KCompositePk() : Serializable {

    @Id
    @Column(name = "ID_1")
    var id1: Long? = null

    @Id
    @Column(name = "ID_2")
    var id2: String? = null

    @Column(name = "DESC")
    var desc: String? = null

    @OneToMany(mappedBy = "compositePk")
    var compositeFks: List<KCompositeFk>? = null

    constructor(id1: Long, id2: String, desc: String) : this() {
        this.id1 = id1
        this.id2 = id2
        this.desc = desc
    }


}
