package pl.exsio.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.metadata.QEntityMetadata;

/**
 * Implementations of this interface should provide all required metadata for given Source
 *
 */
public interface QObjectScanner<T> {

    /**
     * Scan the Source for any Metadata and construct the QEntityMetadata
     * that will server to create dynamic and static EntityQL models
     *
     * @param source - source of databae metadata
     * @return - corresponding Metadata instance
     */
    QEntityMetadata scanEntity(T source);

}
