package pl.exsio.querydsl.entityql.groovy.jpa.entity.spec

import javax.persistence.*

@Entity
@Table(name = "SUB_SPECS")
public class SubSpec {

    @Id
    @Column(name = "SUB_SPEC_ID")
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "SPEC_ID", nullable = true)
    private Spec spec;


    public SubSpec() {
    }

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Spec getSpec() {
        return spec
    }

    void setSpec(Spec spec) {
        this.spec = spec
    }
}
