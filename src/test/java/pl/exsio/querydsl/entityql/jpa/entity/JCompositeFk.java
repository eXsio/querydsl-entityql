package pl.exsio.querydsl.entityql.jpa.entity;


import javax.persistence.*;

@Entity
@Table(name = "JCOMPOSITE_FK")
public class JCompositeFk {

    @Id
    @Column(name = "ID")
    private Long id;

   @ManyToOne
   @JoinColumns({
           @JoinColumn(name = "CPK_ID_1", nullable = false, referencedColumnName = "ID_1"),
           @JoinColumn(name = "CPK_ID_2", nullable = false, referencedColumnName = "ID_2")
   })
    private JCompositePk compositePk;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "SPK_ID_1", nullable = false, referencedColumnName = "ID_1"),
            @JoinColumn(name = "SPK_ID_2", nullable = false, referencedColumnName = "ID_2")
    })
    private JSingularPk singularPk;

    @Column(name = "DESC")
    private String desc;
}
