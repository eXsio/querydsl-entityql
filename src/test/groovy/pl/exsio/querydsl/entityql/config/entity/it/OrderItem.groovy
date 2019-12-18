package pl.exsio.querydsl.entityql.config.entity.it

import javax.persistence.*

@Entity
@Table(name = "ORDER_ITEMS", uniqueConstraints = [
        @UniqueConstraint(name = "unique_book_for_order",
                columnNames = ["BOOK_ID", "ORDER_ID"])
])
public class OrderItem implements Serializable {

    @Id
    @Column(name = "ORDER_ITEM_ID")
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @Column(name = "QTY", nullable = false)
    private Long quantity;

    public OrderItem() {
    }

    public OrderItem(Long id, Book book, Order order, Long quantity) {
        this.id = id;
        this.book = book;
        this.order = order;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Order getShoppingOrder() {
        return order;
    }

    public void setShoppingOrder(Order order) {
        this.order = order;
    }

}
