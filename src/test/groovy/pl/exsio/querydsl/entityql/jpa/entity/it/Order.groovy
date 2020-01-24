package pl.exsio.querydsl.entityql.jpa.entity.it

import javax.persistence.*

@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {

    @Id
    @Column(name = "ORDER_ID")
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "orderReferenced", cascade = CascadeType.ALL)
    private List<OrderItem> itemsReferenced = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setShoppingOrder(this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
