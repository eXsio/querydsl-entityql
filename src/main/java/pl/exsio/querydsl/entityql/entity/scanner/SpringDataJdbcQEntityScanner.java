package pl.exsio.querydsl.entityql.entity.scanner;

import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.core.mapping.JdbcSimpleTypes;
import org.springframework.data.relational.core.mapping.*;
import pl.exsio.querydsl.entityql.entity.metadata.*;
import pl.exsio.querydsl.entityql.ex.MissingIdException;

import javax.annotation.Nonnull;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * QEntityScanner implementation based on Spring Data JDBC.
 */
public class SpringDataJdbcQEntityScanner implements QEntityScanner {

    private final JdbcMappingContext context;

    public SpringDataJdbcQEntityScanner(NamingStrategy namingStrategy) {
        this.context = new JdbcMappingContext(namingStrategy);
    }

    @Override
    public QEntityMetadata scanEntity(Class<?> entityClass) {
        RelationalPersistentEntity<?> requiredPersistentEntity = context.getRequiredPersistentEntity(entityClass);
        QEntityMetadata metadata = new QEntityMetadata(requiredPersistentEntity.getTableName(),
                                                       context.getNamingStrategy().getSchema());
        setColumns(metadata, requiredPersistentEntity);
        return metadata;
    }

    private void setColumns(QEntityMetadata metadata, RelationalPersistentEntity<?> requiredPersistentEntity) {

        Map<String, Integer> fieldNameToIndex = new HashMap<>();

        Field[] declaredFields = requiredPersistentEntity.getType().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            fieldNameToIndex.put(declaredFields[i].getName(), i + 1);
        }

        requiredPersistentEntity.forEach(property -> {
            if ((property.isCollectionLike() || property.isMap()) && !property.getActualType().equals(byte.class)) {
                return;
            }

            if (isOneToOne(property)) {
                addJoinColumn(metadata, property, fieldNameToIndex);
            } else {
                addColumn(metadata, property, fieldNameToIndex);
            }
        });

        if (metadata.getIdColumns().isEmpty()) {
            throw new MissingIdException(requiredPersistentEntity.getType());
        }
    }

    private boolean isOneToOne(RelationalPersistentProperty property) {
        return property.isEntity() && !JdbcSimpleTypes.HOLDER.isSimpleType(property.getType());
    }

    private void addColumn(QEntityMetadata metadata,
                           RelationalPersistentProperty property,
                           Map<String, Integer> fieldNameToIndex) {

        QEntityColumnMetadata columnMetadata = new QEntityColumnMetadata(property.getType(),
                                                                         getComputedType(property),
                                                                         property.getName(),
                                                                         property.getColumnName(),
                                                                         isNullable(property),
                                                                         "",
                                                                         getIndex(property, fieldNameToIndex));

        metadata.addColumn(columnMetadata);

        if (property.isIdProperty()) {
            metadata.addIdColumn(columnMetadata);
        }
    }

    private void addJoinColumn(QEntityMetadata metadata,
                               RelationalPersistentProperty property,
                               Map<String, Integer> fieldNameToIndex) {
        RelationalPersistentEntity<?> persistentEntity = context.getPersistentEntity(property.getActualType());
        PersistentPropertyPathExtension path = new PersistentPropertyPathExtension(context, persistentEntity);
        String reverseColumnName = property.getReverseColumnName(path);
        QEntityJoinColumnMetadata column = new QEntityJoinColumnMetadata(property.getActualType(),
                                                                         property.getName(),
                                                                         reverseColumnName,
                                                                         "",
                                                                         "",
                                                                         isNullable(property),
                                                                         getIndex(property, fieldNameToIndex));
        metadata.addJoinColumn(column);
    }

    private static Class<?> getComputedType(RelationalPersistentProperty property) {
        Class<?> type = property.getType();

        if (type.isArray()) {
            return Array.class;
        }

        if (type.isEnum()) {
            return Enum.class;
        }

        return type;
    }

    private Integer getIndex(RelationalPersistentProperty property, Map<String, Integer> fieldNameToIndex) {
        return fieldNameToIndex.get(property.getName());
    }

    private boolean isNullable(RelationalPersistentProperty property) {
        return !property.isAnnotationPresent(Nonnull.class);
    }
}
