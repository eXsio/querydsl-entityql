package pl.exsio.querydsl.entityql.jdbc.entity.it

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

import java.time.Instant

public class User<T> {

    enum Type {
        ADMIN, CLIENT
    }

    @Column("USER_ID")
    @Id
    private final Long id;
    private final String name;
    private final Order order;
    private final Type typeStr
    private final Type typeDef
    private final T createdBy
    private final Instant createdAt;

    User(Long id, String name, Order order, Type typeStr, Type typeDef, T createdBy, Instant createdAt) {
        this.id = id
        this.name = name
        this.order = order
        this.typeStr = typeStr
        this.typeDef = typeDef
        this.createdBy = createdBy
        this.createdAt = createdAt
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Order getOrder() {
        return order;
    }

    Type getType() {
        return typeStr
    }

    T getCreatedBy() {
        return createdBy
    }
}
