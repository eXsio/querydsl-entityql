package pl.exsio.querydsl.entityql.jpa.entity.it

import pl.exsio.querydsl.entityql.config.enums.by_name.UserTypeByName
import pl.exsio.querydsl.entityql.config.enums.by_ordinal.UserTypeByOrdinal

import javax.persistence.*

@Entity
@Table(name = "USERS")
public class User<T> {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Order order;

    @Column(name = "TYPE_STR", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserTypeByName typeStr

    @Column(name = "TYPE_ORD", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserTypeByOrdinal typeOrd

    @Column(name = "CREATED_BY", columnDefinition = "varchar(15)")
    @org.hibernate.annotations.Type(type = "java.lang.String")
    private T createdBy

    @Column(name="CREATED_AT")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name="ENABLED")
    private Boolean enabled;

    public User() {
    }

    public User(String name) {
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    UserTypeByName getTypeStr() {
        return typeStr
    }

    void setTypeStr(UserTypeByName typeStr) {
        this.typeStr = typeStr
    }

    UserTypeByOrdinal getTypeOrd() {
        return typeOrd
    }

    void setTypeOrd(UserTypeByOrdinal typeOrd) {
        this.typeOrd = typeOrd
    }


    Date getCreatedAt() {
        return createdAt
    }

    void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt
    }

    void setCreatedBy(T createdBy) {
        this.createdBy = createdBy
    }
}
