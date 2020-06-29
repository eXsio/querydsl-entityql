package pl.exsio.querydsl.entityql.java.jpa.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JORDERS")
public class JOrder implements Serializable {

    @Id
    @Column(name = "ORDER_ID")
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private JUser user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<JOrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "orderReferenced", cascade = CascadeType.ALL)
    private List<JOrderItem> itemsReferenced = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<JOrderItem> getItems() {
        return items;
    }

    public void setItems(List<JOrderItem> items) {
        this.items = items;
    }

    public void addItem(JOrderItem item) {
        items.add(item);
        item.setShoppingOrder(this);
    }

    public JUser getUser() {
        return user;
    }

    public void setUser(JUser user) {
        this.user = user;
    }

}
