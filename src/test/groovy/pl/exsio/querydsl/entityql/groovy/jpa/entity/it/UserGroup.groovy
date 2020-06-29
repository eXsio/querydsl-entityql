package pl.exsio.querydsl.entityql.groovy.jpa.entity.it

import org.hibernate.annotations.Immutable

import javax.persistence.*

@Entity
@Immutable
@Table(name = "USERS_GROUPS")
public class UserGroup implements Serializable {

    @Id
    @Column(name = "GROUP_ID", nullable = false, updatable = false, insertable = false)
    private Long groupId;

    @Id
    @Column(name = "USER_ID", nullable = false, updatable = false, insertable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
