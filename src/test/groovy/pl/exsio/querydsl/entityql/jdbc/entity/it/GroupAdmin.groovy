package pl.exsio.querydsl.entityql.jdbc.entity.it

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

public class GroupAdmin {

    @Id
    @Column("GA_ID")
    private final Long id;
    private final String name;

    public GroupAdmin(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
