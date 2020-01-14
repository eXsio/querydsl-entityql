package pl.exsio.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.metadata.*;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;
import pl.exsio.querydsl.entityql.ex.MissingIdException;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
            throw new InvalidArgumentException(String.format("Entity class %s must have @Table annotation", entityClass.getName()));
        }
        return table;
    }

    private void setColumns(QEntityMetadata metadata, Class<?> entityClass) {
        List<String> ids = new LinkedList<>();
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
            Id id = field.getDeclaredAnnotation(Id.class);
            QEntityColumnMetadata columnMetadata = new QEntityColumnMetadata(field.getType(), getComputedType(field), field.getName(),
                    column.name(), column.nullable(), column.columnDefinition(), index);
            metadata.addColumn(columnMetadata);
            if (id != null) {
                metadata.addIdColumn(columnMetadata);
            }
        } else {
            JoinColumn joinColumn = field.getDeclaredAnnotation(JoinColumn.class);
            if (joinColumn != null) {
                if (field.getDeclaredAnnotation(OneToMany.class) == null) {
                    metadata.addJoinColumn(new QEntityJoinColumnMetadata(field.getType(), field.getName(), joinColumn.name(),
                            joinColumn.columnDefinition(), joinColumn.referencedColumnName(), joinColumn.nullable(), index));
                }
            } else {
                JoinColumns joinColumns = field.getDeclaredAnnotation(JoinColumns.class);
                if (joinColumns != null) {
                    if (field.getDeclaredAnnotation(OneToMany.class) == null) {
                        QEntityCompositeJoinColumnMetadata compositeJoinColumn = new QEntityCompositeJoinColumnMetadata(field.getType(), field.getName(), index);
                        Arrays.stream(joinColumns.value())
                                .map(jc -> new QEntityCompositeJoinColumnItemMetadata(jc.name(), jc.columnDefinition(), jc.referencedColumnName(), jc.nullable()))
                                .forEach(compositeJoinColumn::addItem);
                        metadata.addCompositeJoinColumn(compositeJoinColumn);
                    }
                }
            }
        }
    }

    private static Class<?> getComputedType(Field field) {
        Class<?> type = field.getType();
        if (type.isArray()) {
            type = Array.class;
        } else if (type.isEnum()) {
            Enumerated enumerated = field.getDeclaredAnnotation(Enumerated.class);
            if (enumerated != null) {
                type = enumerated.value().equals(EnumType.ORDINAL) ? Integer.class : Enum.class;
            } else {
                type = Integer.class;
            }
        }
        return type;
    }
}
