package pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

public class Book {

    @Id
    @Column("BOOK_ID")
    private final Long id;
    private final String name;
    private final String desc;
    private final BigDecimal price;

    public Book(Long id, String name, String desc, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    String getDesc() {
        return desc
    }
}
