package pl.exsio.querydsl.entityql.config.entity.spec

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
    private Spec unit;


    public SubSpec() {
    }

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Spec getUnit() {
        return unit
    }

    void setUnit(Spec unit) {
        this.unit = unit
    }
}
