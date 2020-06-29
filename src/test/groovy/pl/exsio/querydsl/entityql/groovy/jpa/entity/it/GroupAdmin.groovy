package pl.exsio.querydsl.entityql.groovy.jpa.entity.it

import javax.persistence.*

@Entity
@Table(name = "GROUP_ADMINS")
public class GroupAdmin {

    @Id
    @Column(name = "GA_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String name;


    public GroupAdmin() {
    }

    public GroupAdmin(String name) {
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
}
