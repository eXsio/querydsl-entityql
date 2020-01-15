package pl.exsio.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.metadata.*;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;
import pl.exsio.querydsl.entityql.ex.MissingIdException;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Default QEntityScanner implementation based on JPA Metadata Annotations
 */
public class JpaQEntityScanner implements QEntityScanner {

    @Override
    public QEntityMetadata scanEntity(Class<?> entityClass) {
        Table table = getTable(entityClass);
        QEntityMetadata metadata = new QEntityMetadata(table.name(), table.schema());
        setColumns(metadata, entityClass);
        return metadata;
    }

    private Table getTable(Class<?> entityClass) {
        Table table = entityClass.getDeclaredAnnotation(Table.class);
        if (table == null) {
            throw new InvalidArgumentException(String.format("Entity class %s must have @Table annotation",
                    entityClass.getName())
            );
        }
        return table;
    }

    private void setColumns(QEntityMetadata metadata, Class<?> entityClass) {
        Class<?> prevClass = entityClass;
        while (!prevClass.equals(Object.class)) {
            for (int i = 0; i < prevClass.getDeclaredFields().length; i++) {
                parse(prevClass.getDeclaredFields()[i], i + 1, metadata);
            }
            prevClass = prevClass.getSuperclass();
        }
        if (metadata.getIdColumns().isEmpty()) {
            throw new MissingIdException(entityClass);
        }
    }

    private void parse(Field field, int index, QEntityMetadata metadata) {
        Column column = field.getDeclaredAnnotation(Column.class);
        if (column != null) {
            addColumn(field, index, metadata, column);
        } else {
            JoinColumn joinColumn = field.getDeclaredAnnotation(JoinColumn.class);
            if (joinColumn != null) {
                if (field.getDeclaredAnnotation(OneToMany.class) == null) {
                    addJoinColumn(field, index, metadata, joinColumn);
                }
            } else {
                JoinColumns joinColumns = field.getDeclaredAnnotation(JoinColumns.class);
                if (joinColumns != null) {
                    if (field.getDeclaredAnnotation(OneToMany.class) == null) {
                        addCompositeJoinColumn(field, index, metadata, joinColumns);
                    }
                }
            }
        }
    }

    private void addColumn(Field field, int index, QEntityMetadata metadata, Column column) {
        Id id = field.getDeclaredAnnotation(Id.class);
        QEntityColumnMetadata columnMetadata = new QEntityColumnMetadata(field.getType(), field.getName(),
                column.name(), column.nullable(), column.columnDefinition(), index);
        metadata.addColumn(columnMetadata);
        if (id != null) {
            metadata.addIdColumn(columnMetadata);
        }
    }

    private void addJoinColumn(Field field, int index, QEntityMetadata metadata, JoinColumn joinColumn) {
        metadata.addJoinColumn(new QEntityJoinColumnMetadata(field.getType(), field.getName(), joinColumn.name(),
                joinColumn.columnDefinition(), joinColumn.referencedColumnName(), joinColumn.nullable(), index));
    }

    private void addCompositeJoinColumn(Field field, int index, QEntityMetadata metadata, JoinColumns joinColumns) {
        QEntityCompositeJoinColumnMetadata compositeJoinColumn = new QEntityCompositeJoinColumnMetadata(field.getType(), field.getName(), index);
        Arrays.stream(joinColumns.value())
                .map(jc -> new QEntityCompositeJoinColumnItemMetadata(jc.name(), jc.columnDefinition(), jc.referencedColumnName(), jc.nullable()))
                .forEach(compositeJoinColumn::addItem);
        metadata.addCompositeJoinColumn(compositeJoinColumn);
    }
}
