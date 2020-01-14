package pl.exsio.querydsl.entityql;

import pl.exsio.querydsl.entityql.entity.metadata.QEntityMetadata;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;

import java.util.HashMap;
import java.util.Map;

class QFactory<E> {

    private final Class<E> entityClass;

    private final QEntityScanner scanner;

    private final QEntityMetadata metadata;

    private static final Map<Class<?>, QFactory<?>> instances = new HashMap<>();

    private QFactory(Class<E> entityClass, QEntityScanner scanner, QEntityMetadata metadata) {
        this.entityClass = entityClass;
        this.scanner = scanner;
        this.metadata = metadata;
    }

    @SuppressWarnings(value = "unchecked")
    static <E> QFactory<E> get(Class<E> entityClass, QEntityScanner scanner) {
        return (QFactory<E>) instances.compute(entityClass,
                (eClass, qFactory) -> qFactory == null ? fetch(eClass, scanner) : qFactory
        );
    }

    Q<E> create(boolean withMappings) {
        return create(metadata.getTableName(), withMappings);
    }

    private static <E> QFactory<E> fetch(Class<E> entityClass, QEntityScanner scanner) {
        return new QFactory<>(entityClass, scanner, scanner.scanEntity(entityClass));
    }

    Q<E> create(String variable, boolean withMappings) {
        Q<E> type = new Q<>(entityClass, variable, metadata.getSchemaName(), metadata.getTableName(), scanner);
        metadata.getColumns().forEach(type::addColumn);
        if (withMappings) {
            metadata.getJoinColumns().forEach(type::addJoinColumn);
            metadata.getCompositeJoinColumns().forEach(type::addCompositeJoinColumn);
        }
        type.addPrimaryKey(metadata.getIdColumns());
        return type;
    }

    public QEntityMetadata getMetadata() {
        return metadata;
    }
}
