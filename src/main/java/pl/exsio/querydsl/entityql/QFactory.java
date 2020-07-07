package pl.exsio.querydsl.entityql;

import com.querydsl.core.Tuple;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityMetadata;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;
import pl.exsio.querydsl.entityql.entity.scanner.QObjectScanner;
import pl.exsio.querydsl.entityql.entity.scanner.TableScanner;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.Table;

import java.util.HashMap;
import java.util.Map;

class QFactory<E> {

    private final Class<E> entityClass;

    private final QObjectScanner scanner;

    private final QEntityMetadata metadata;

    private static final Map<Object, QFactory<?>> instances = new HashMap<>();

    private QFactory(Class<E> entityClass, QObjectScanner scanner, QEntityMetadata metadata) {
        this.entityClass = entityClass;
        this.scanner = scanner;
        this.metadata = metadata;
    }

    @SuppressWarnings(value = "unchecked")
    static <E> QFactory<E> get(Class<E> entityClass, QEntityScanner scanner) {
        return (QFactory<E>) instances.compute(entityClass,
                (key, qFactory) -> qFactory == null ? fetch(entityClass, entityClass, scanner) : qFactory
        );
    }

    static QFactory get(Table table, TableScanner scanner) {
        return instances.compute(table.getTableName(),
                (key, qFactory) -> qFactory == null ? fetch(Tuple.class, table, scanner) : qFactory
        );
    }

    Q<E> create(boolean withMappings) {
        return create(metadata.getTableName(), withMappings);
    }

    private static <T> QFactory fetch(Class<?> clazz, T source, QObjectScanner<T> scanner) {
        QEntityMetadata metadata = scanner.scanEntity(source);
        return new QFactory(clazz, scanner, metadata);
    }

    Q<E> create(String variable, boolean withMappings) {
        Q<E> type = new Q<>(entityClass, variable, metadata.getSchemaName(), metadata.getTableName(), scanner);
        metadata.getColumns().forEach(type::addColumn);
        if (withMappings) {
            metadata.getJoinColumns().forEach(type::addJoinColumn);
            metadata.getCompositeJoinColumns().forEach(type::addCompositeJoinColumn);
            metadata.getInverseJoinColumns().forEach(type::addInverseJoinColumn);
            metadata.getInverseCompositeJoinColumns().forEach(type::addInverseCompositeJoinColumn);
        }
        type.addPrimaryKey(metadata.getIdColumns());
        return type;
    }

    public QEntityMetadata getMetadata() {
        return metadata;
    }
}
