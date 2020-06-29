package pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import pl.exsio.querydsl.entityql.groovy.config.enums.by_name.UserTypeByName
import pl.exsio.querydsl.entityql.groovy.config.enums.by_ordinal.UserTypeByOrdinal

public class User<T> {

    @Column("USER_ID")
    @Id
    private final Long id;
    private final String name;
    private final Order order;
    private final UserTypeByName typeStr
    private final UserTypeByOrdinal typeOrd
    private final T createdBy
    private final Date createdAt;
    private final Boolean enabled;

    User(Long id, String name, Order order, UserTypeByName typeStr, UserTypeByOrdinal typeOrd, T createdBy, Date createdAt, Boolean enabled) {
        this.id = id
        this.name = name
        this.order = order
        this.typeStr = typeStr
        this.typeOrd = typeOrd
        this.createdBy = createdBy
        this.createdAt = createdAt
        this.enabled = enabled;
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

    UserTypeByName getTypeStr() {
        return typeStr
    }

    UserTypeByOrdinal getTypeOrd() {
        return typeOrd
    }

    T getCreatedBy() {
        return createdBy
    }
}
