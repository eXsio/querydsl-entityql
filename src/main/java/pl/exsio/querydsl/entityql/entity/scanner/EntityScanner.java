package pl.exsio.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.metadata.EntityMetadata;

public interface EntityScanner {

    EntityMetadata scanEntity(Class<?> entityClass);
}
