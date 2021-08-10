package pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

public class OrderItem implements Serializable {

    @Id
    @Column("ORDER_ITEM_ID")
    private final Long id;

    @Column("BOOK_ID")
    private final Book book;

    @Column("ITEM_ORDER_ID")
    private final Order order;

    @Column("QTY")
    private final Long quantity;

    public OrderItem(Long id, Book book, Order order, Long quantity) {
        this.id = id;
        this.book = book;
        this.order = order;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Order getShoppingOrder() {
        return order;
    }
}
