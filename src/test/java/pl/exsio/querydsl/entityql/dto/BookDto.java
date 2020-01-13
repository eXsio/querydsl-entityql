package pl.exsio.querydsl.entityql.dto;

import java.math.BigDecimal;

public class BookDto {
    private final Long id;
    private final String name;
    private final String desc;
    private final BigDecimal price;

    public BookDto(Long id, String name, String desc, BigDecimal price) {
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

    public String getDesc() {
        return desc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                '}';
    }
}
