package pl.exsio.querydsl.entityql.config.dto

import pl.exsio.querydsl.entityql.config.enums.by_name.UserTypeByName

class UserDto {
    
    private final Long id;
    
    private final String name;
    
    private final UserTypeByName type;

    private final Boolean enabled;

    UserDto(Long id, String name, UserTypeByName type, Boolean enabled) {
        this.id = id
        this.name = name
        this.type = type
        this.enabled = enabled;
    }

    Long getId() {
        return id
    }

    String getName() {
        return name
    }

    UserTypeByName getType() {
        return type
    }

    Boolean getEnabled() {
        return enabled
    }
}
