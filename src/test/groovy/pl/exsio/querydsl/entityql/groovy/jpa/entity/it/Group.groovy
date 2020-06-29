package pl.exsio.querydsl.entityql.groovy.jpa.entity.it

import javax.persistence.*

@Entity
@Table(name = "GROUPS")
public class Group {

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
    private Set<User> users;

    @ManyToOne
    @JoinColumn(name = "ADMIN_NAME", nullable = false, referencedColumnName = "NAME")
    private GroupAdmin admin;

    public Group() {
    }

    public Group(Long id, String name) {
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

    GroupAdmin getAdmin() {
        return admin
    }

    void setAdmin(GroupAdmin admin) {
        this.admin = admin
    }
}
