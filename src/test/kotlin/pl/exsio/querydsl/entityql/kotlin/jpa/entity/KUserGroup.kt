package pl.exsio.querydsl.entityql.kotlin.jpa.entity

import org.hibernate.annotations.Immutable
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KGroup
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KUser
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(name = "USERS_GROUPS")
class KUserGroup() : Serializable {

    @Id
    @Column(name = "GROUP_ID", nullable = false, updatable = false, insertable = false)
    var groupId: Long? = null

    @Id
    @Column(name = "USER_ID", nullable = false, updatable = false, insertable = false)
    var userId: Long? = null

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    var group: KGroup? = null

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    var user: KUser<Any>? = null
}
