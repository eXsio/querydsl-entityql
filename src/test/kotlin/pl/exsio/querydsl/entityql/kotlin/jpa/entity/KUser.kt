package pl.exsio.querydsl.entityql.kotlin.jpa.entity


import java.util.*
import javax.persistence.*

@Entity
@Table(name = "USERS")
public class KUser<T>() {

    enum class Type {
        ADMIN, CLIENT
    }

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
    var typeStr: Type? = null

    @Column(name = "TYPE_ORD", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    var typeOrd: Type? = null

    @Column(name = "CREATED_BY", columnDefinition = "varchar(15)")
    @org.hibernate.annotations.Type(type = "java.lang.String")
    var createdBy: T? = null

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.DATE)
    var createdAt: Date? = null

    @Column(name = "ENABLED")
    var enabled: Boolean? = null
}
