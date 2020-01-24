package pl.exsio.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.metadata.*;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;
import pl.exsio.querydsl.entityql.ex.MissingIdException;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

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
            OneToMany oneToMany = field.getDeclaredAnnotation(OneToMany.class);
            JoinColumn joinColumn = field.getDeclaredAnnotation(JoinColumn.class);
            if (joinColumn != null && oneToMany == null) {
                metadata.addJoinColumn(getJoinColumnMetadata(field.getType(), field.getName(), index, joinColumn));
            } else {
                JoinColumns joinColumns = field.getDeclaredAnnotation(JoinColumns.class);
                if (joinColumns != null && oneToMany == null) {
                    metadata.addCompositeJoinColumn(getCompositeJoinColumnMetadata(field.getType(), field.getName(), index, joinColumns));
                } else if (oneToMany != null) {
                    parseOneToMany(field, index, metadata, oneToMany, joinColumn, joinColumns);
                } else {
                    OneToOne oneToOne = field.getDeclaredAnnotation(OneToOne.class);
                    if (oneToOne != null && !oneToOne.mappedBy().equals("")) {
                        parseOneToOne(field, index, metadata, oneToOne);
                    }
                }
            }
        }
    }

    private void parseOneToOne(Field field, int index, QEntityMetadata metadata, OneToOne oneToOne) {
        Class<?> foreignType = field.getType();
        Field foreignField = getForeignField(foreignType, oneToOne.mappedBy());
        JoinColumn foreignJoinColumn = foreignField.getDeclaredAnnotation(JoinColumn.class);
        if (foreignJoinColumn != null) {
            metadata.addInverseJoinColumn(getJoinColumnMetadata(foreignType, field.getName(), index, foreignJoinColumn));
        } else {
            JoinColumns foreignJoinColumns = foreignField.getDeclaredAnnotation(JoinColumns.class);
            if (foreignJoinColumns != null) {
                metadata.addInverseCompositeJoinColumn(getCompositeJoinColumnMetadata(foreignType, field.getName(), index, foreignJoinColumns));
            }
        }
    }

    private void parseOneToMany(Field field, int index, QEntityMetadata metadata, OneToMany oneToMany, JoinColumn joinColumn, JoinColumns joinColumns) {
        if(!Collection.class.isAssignableFrom(field.getType())) {
            return;
        }
        Class<?> foreignType = getCollectionType(field);
        if (!oneToMany.mappedBy().equals("")) {
            Field foreignField = getForeignField(foreignType, oneToMany.mappedBy());
            JoinColumn foreignJoinColumn = foreignField.getDeclaredAnnotation(JoinColumn.class);
            if (foreignJoinColumn != null) {
                metadata.addInverseJoinColumn(getJoinColumnMetadata(foreignType, field.getName(), index, foreignJoinColumn));
            } else {
                JoinColumns foreignJoinColumns = foreignField.getDeclaredAnnotation(JoinColumns.class);
                if (foreignJoinColumns != null) {
                    metadata.addInverseCompositeJoinColumn(getCompositeJoinColumnMetadata(foreignType, field.getName(), index, foreignJoinColumns));
                }
            }
        } else if (joinColumn != null) {
            metadata.addInverseJoinColumn(getJoinColumnMetadata(foreignType, field.getName(), index, joinColumn));
        } else if (joinColumns != null) {
            metadata.addInverseCompositeJoinColumn(getCompositeJoinColumnMetadata(foreignType, field.getName(), index, joinColumns));
        }
    }

    private Field getForeignField(Class<?> foreignType, String mappedBy) {
        try {
            return foreignType.getDeclaredField(mappedBy);
        } catch (NoSuchFieldException e) {
            throw new InvalidArgumentException(String.format("Unable to locate field '%s' on class '%s'", mappedBy, foreignType.getName()));
        }
    }

    private Class<?> getCollectionType(Field field) {
        Type type = field.getGenericType();
        ParameterizedType pt = (ParameterizedType) type;
        return (Class<?>) pt.getActualTypeArguments()[0];
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

    private QEntityJoinColumnMetadata getJoinColumnMetadata(Class<?> fieldType, String fieldName, int index, JoinColumn joinColumn) {
        return new QEntityJoinColumnMetadata(fieldType, fieldName, joinColumn.name(),
                joinColumn.columnDefinition(), joinColumn.referencedColumnName(), joinColumn.nullable(), index);
    }

    private QEntityCompositeJoinColumnMetadata getCompositeJoinColumnMetadata(Class<?> fieldType, String fieldName, int index, JoinColumns joinColumns) {
        QEntityCompositeJoinColumnMetadata compositeJoinColumn = new QEntityCompositeJoinColumnMetadata(fieldType, fieldName, index);
        Arrays.stream(joinColumns.value())
                .map(jc -> new QEntityCompositeJoinColumnItemMetadata(jc.name(), jc.columnDefinition(), jc.referencedColumnName(), jc.nullable()))
                .forEach(compositeJoinColumn::addItem);
        return compositeJoinColumn;
    }
}
