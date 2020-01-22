package pl.exsio.querydsl.entityql.config.dto

import pl.exsio.querydsl.entityql.config.enums.by_name.UserTypeByName

class UserDto {
    
    private final Long id;
    
    private final String name;
    
    private final UserTypeByName type;

    UserDto(Long id, String name, UserTypeByName type) {
        this.id = id
        this.name = name
        this.type = type
    }
}
