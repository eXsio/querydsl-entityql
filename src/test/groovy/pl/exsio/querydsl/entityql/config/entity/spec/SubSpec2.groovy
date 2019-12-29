package pl.exsio.querydsl.entityql.config.entity.spec

import javax.persistence.*

@Entity
@Table(name = "SUB_SPECS2")
public class SubSpec2 {

    @Id
    @Column(name = "SUB_SPEC_ID")
    @GeneratedValue
    private Long id;

    public SubSpec2() {
    }

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }
}
