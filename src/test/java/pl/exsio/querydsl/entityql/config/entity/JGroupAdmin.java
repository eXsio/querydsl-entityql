package pl.exsio.querydsl.entityql.config.entity;

import javax.persistence.*;

@Entity
@Table(name = "JGROUP_ADMINS")
public class JGroupAdmin {

    @Id
    @Column(name = "GA_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String name;


    public JGroupAdmin() {
    }

    public JGroupAdmin(String name) {
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
