package pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it

import org.springframework.data.annotation.Id

public class Group {

    @Id
    private Long id;
    private String name;
    private GroupAdmin admin;

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GroupAdmin getAdmin() {
        return admin
    }
}
