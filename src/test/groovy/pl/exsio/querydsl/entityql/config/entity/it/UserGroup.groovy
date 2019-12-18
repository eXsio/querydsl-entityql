package pl.exsio.querydsl.entityql.config.entity.it

import org.hibernate.annotations.Immutable

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "USERS_GROUPS")
public class UserGroup implements Serializable {

    @Id
    @Column(name = "GROUP_ID", nullable = false)
    private Long groupId;

    @Id
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
}
