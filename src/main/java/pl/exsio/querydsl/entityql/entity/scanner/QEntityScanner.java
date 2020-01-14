package pl.exsio.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.metadata.QEntityMetadata;

/**
 * Implementations of this interface should provide all required metadata for given Entity Class
 */
public interface QEntityScanner {

    /**
     * Scan the Entity class for any Metadata and construct the QEntityMetadata
     * that will server to create dynamic and static EntityQL models
     *
     * @param entityClass - source Entity class
     * @return - corresponding Metadata instance
     */
    QEntityMetadata scanEntity(Class<?> entityClass);
}
