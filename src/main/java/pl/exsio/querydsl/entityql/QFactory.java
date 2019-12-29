package pl.exsio.querydsl.entityql;

import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;
import pl.exsio.querydsl.entityql.ex.MissingIdException;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class QFactory<E> {

    private final Class<E> entityClass;

    private Table table;

    private Map<Field, Map.Entry<Integer, Column>> columns = new LinkedHashMap<>();

    private Map<Field, Map.Entry<Integer, JoinColumn>> joinColumns = new LinkedHashMap<>();

    private Map.Entry<Field, Column> id;

    private static final Map<Class<?>, QFactory<?>> instances = new HashMap<>();

    private QFactory(Class<E> entityClass) {
        this.entityClass = entityClass;
        scanEntityClass();
    }

    @SuppressWarnings(value = "unchecked")
    static <E> QFactory<E> get(Class<E> entityClass) {
        return (QFactory<E>) instances.compute(entityClass,
                (eClass, qFactory) -> qFactory == null ? fetch(eClass) : qFactory
        );
    }

    Q<E> create(boolean withMappings) {
        return create(table.name(), withMappings);
    }

    private static <E> QFactory<E> fetch(Class<E> entityClass) {
        return new QFactory<>(entityClass);
    }

    private void scanEntityClass() {
        setTable();
        setColumns();
    }

    private void setColumns() {
        Class<?> prevClass = entityClass;
        while (!prevClass.equals(Object.class)) {
            for (int i = 0; i < prevClass.getDeclaredFields().length; i++) {
                parse(prevClass.getDeclaredFields()[i], i + 1);
            }
            prevClass = prevClass.getSuperclass();
        }
        if (id == null) {
            throw new MissingIdException(entityClass);
        }
    }

    private void parse(Field field, int index) {
        Column column = field.getDeclaredAnnotation(Column.class);
        if (column != null) {
            Id id = field.getDeclaredAnnotation(Id.class);
            if (id != null) {
                this.id = new AbstractMap.SimpleImmutableEntry<>(field, column);
            }
            this.columns.put(field, new AbstractMap.SimpleImmutableEntry<>(index, column));
        } else {
            JoinColumn joinColumn = field.getDeclaredAnnotation(JoinColumn.class);
            if (joinColumn != null) {
                if (field.getDeclaredAnnotation(OneToMany.class) == null) {
                    this.joinColumns.put(field, new AbstractMap.SimpleImmutableEntry<>(index, joinColumn));
                }
            }
        }
    }

    private void setTable() {
        Table table = entityClass.getDeclaredAnnotation(Table.class);
        if (table == null) {
            throw new InvalidArgumentException(String.format("Entity class %s must have @Table annotation", entityClass.getName()));
        }
        this.table = table;
    }

    Q<E> create(String variable, boolean withMappings) {
        Q<E> type = new Q<>(entityClass, variable, table.schema(), table.name());
        columns.forEach((field, column) -> type.addColumn(field, column.getValue(), column.getKey(), field.equals(this.id.getKey())));
        if (withMappings) {
            joinColumns.forEach((field, column) -> type.addJoinColumn(field, column.getValue(), column.getKey()));
        }
        return type;
    }

    Map<Field, Map.Entry<Integer, Column>> getColumns() {
        return columns;
    }
}
