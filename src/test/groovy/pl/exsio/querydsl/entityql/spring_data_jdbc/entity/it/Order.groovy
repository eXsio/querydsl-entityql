package pl.exsio.querydsl.entityql.spring_data_jdbc.entity.it

import org.springframework.data.annotation.Id

import javax.annotation.Nonnull

public class Order implements Serializable {

    @Id
    private final Long id;

    @Nonnull
    private final User user;
    private List<OrderItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public User getUser() {
        return user;
    }
}
