package pl.exsio.querydsl.entityql.java.jpa.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Immutable
@Table(name = "JUSERS_GROUPS")
public class JUserGroup implements Serializable {

    @Id
    @Column(name = "GROUP_ID", nullable = false, updatable = false, insertable = false)
    private Long groupId;

    @Id
    @Column(name = "USER_ID", nullable = false, updatable = false, insertable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private JGroup group;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private JUser user;
}
