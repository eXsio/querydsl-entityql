package pl.exsio.querydsl.entityql.config.entity.it

import javax.persistence.*

@Entity
@Table(name = "USERS")
public class User<T> {

    enum Type {
        ADMIN, CLIENT
    }

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Order order;

    @Column(name = "USER_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type typeStr

    @Column(name = "USER_TYPE_ORD", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Type typeOrd

    @Column(name = "USER_TYPE_DEF", nullable = false)
    private Type typeDef

    @Column(name = "CREATED_BY", columnDefinition = "varchar(15)")
    @org.hibernate.annotations.Type(type = "java.lang.String")
    private T createdBy

    @Column(name="CREATED_AT")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

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

    Type getType() {
        return typeStr
    }

    void setType(Type type) {
        this.typeStr = type
        this.typeDef = type
        this.typeOrd = type
    }

    T getCreatedBy() {
        return createdBy
    }

    void setCreatedBy(T createdBy) {
        this.createdBy = createdBy
    }
}
