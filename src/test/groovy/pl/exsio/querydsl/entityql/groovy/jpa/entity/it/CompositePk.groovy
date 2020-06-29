package pl.exsio.querydsl.entityql.groovy.jpa.entity.it

import javax.persistence.*

@Entity
@Table(name = "COMPOSITE_PK")
public class CompositePk implements Serializable {

    @Id
    @Column(name = "ID_1")
    private Long id1;

    @Id
    @Column(name = "ID_2")
    private String id2;

    @Column(name = "DESC")
    private String desc;

    @OneToMany(mappedBy = "compositePk")
    private List<CompositeFk> compositeFks

    public CompositePk() {
    }

    public CompositePk(Long id1, String id2, String desc) {
        this.id1 = id1;
        this.id2 = id2;
        this.desc = desc;
    }

    Long getId1() {
        return id1
    }

    String getId2() {
        return id2
    }

    String getDesc() {
        return desc
    }
}
