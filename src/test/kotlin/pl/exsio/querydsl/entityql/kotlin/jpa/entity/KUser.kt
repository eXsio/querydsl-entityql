package pl.exsio.querydsl.entityql.kotlin.jpa.entity


import pl.exsio.querydsl.entityql.kotlin.config.enums.by_name.KUserTypeByName
import pl.exsio.querydsl.entityql.kotlin.config.enums.by_ordinal.KUserTypeByOrdinal
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "USERS")
class KUser<T>() {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "NAME")
    var name: String? = null

    @OneToOne(mappedBy = "user")
    var order: KOrder? = null

    @Column(name = "TYPE_STR", nullable = false)
    @Enumerated(EnumType.STRING)
    var typeStr: KUserTypeByName? = null

    @Column(name = "TYPE_ORD", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    var typeOrd: KUserTypeByOrdinal? = null

    @Column(name = "CREATED_BY", columnDefinition = "varchar(15)")
    @org.hibernate.annotations.Type(type = "java.lang.String")
    var createdBy: T? = null

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.DATE)
    var createdAt: Date? = null

    @Column(name = "ENABLED")
    var enabled: Boolean? = null
}
