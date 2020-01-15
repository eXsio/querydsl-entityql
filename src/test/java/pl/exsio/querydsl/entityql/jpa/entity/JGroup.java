package pl.exsio.querydsl.entityql.jpa.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "JGROUPS")
public class JGroup {

    @Id
    @Column(name = "GROUP_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "USERS_GROUPS",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    private Set<JUser> users;

    @ManyToOne
    @JoinColumn(name = "ADMIN_NAME", nullable = false, referencedColumnName = "NAME")
    private JGroupAdmin admin;

    public JGroup() {
    }

    public JGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    JGroupAdmin getAdmin() {
        return admin;
    }

    void setAdmin(JGroupAdmin admin) {
        this.admin = admin;
    }
}
