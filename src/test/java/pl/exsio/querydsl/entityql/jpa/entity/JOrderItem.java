package pl.exsio.querydsl.entityql.jpa.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "JORDER_ITEMS", uniqueConstraints = {
        @UniqueConstraint(name = "junique_book_for_order",
                columnNames = {"BOOK_ID", "ORDER_ID"})
})
public class JOrderItem implements Serializable {

    @Id
    @Column(name = "ORDER_ITEM_ID")
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private JBook book;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private JOrder order;

    @Column(name = "QTY", nullable = false)
    private Long quantity;

    public JOrderItem() {
    }

    public JOrderItem(Long id, JBook book, JOrder order, Long quantity) {
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

    public JBook getBook() {
        return book;
    }

    public void setBook(JBook book) {
        this.book = book;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public JOrder getShoppingOrder() {
        return order;
    }

    public void setShoppingOrder(JOrder order) {
        this.order = order;
    }

}
