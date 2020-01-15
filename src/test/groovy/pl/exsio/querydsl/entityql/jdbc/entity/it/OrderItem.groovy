package pl.exsio.querydsl.entityql.jdbc.entity.it

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

import javax.annotation.Nonnull

public class OrderItem implements Serializable {

    @Id
    @Column("ORDER_ITEM_ID")
    private final Long id;
    @Nonnull
    @Column("BOOK_ID")
    private final Book book;
    @Nonnull
    private final Order order;
    @Nonnull
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
