package pl.exsio.querydsl.entityql.kotlin.jpa.entity

import javax.persistence.*

@Entity
@Table(name = "GROUPS")
class KGroup() {

    @Id
    @Column(name = "GROUP_ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "NAME")
    var name: String? = null

    @ManyToMany
    @JoinTable(
            name = "USERS_GROUPS",
            joinColumns = [JoinColumn(name = "GROUP_ID")],
            inverseJoinColumns = [JoinColumn(name = "USER_ID")]
    )
    var users: Set<KUser<Any>>? = null

    @ManyToOne
    @JoinColumn(name = "ADMIN_NAME", nullable = false, referencedColumnName = "NAME")
    var admin: KGroupAdmin? = null

    constructor(id: Long, name: String) : this() {
        this.id = id
        this.name = name
    }


}
