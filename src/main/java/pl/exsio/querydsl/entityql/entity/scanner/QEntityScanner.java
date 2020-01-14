package pl.exsio.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.metadata.QEntityMetadata;

public interface QEntityScanner {

    QEntityMetadata scanEntity(Class<?> entityClass);
}
