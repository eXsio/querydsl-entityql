package pl.exsio.querydsl.entityql.entity.scanner;

import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.core.mapping.JdbcSimpleTypes;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityJoinColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityMetadata;
import pl.exsio.querydsl.entityql.ex.MissingIdException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        QEntityMetadata metadata = new QEntityMetadata(requiredPersistentEntity.getTableName().getReference(),
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
                property.getName(),
                property.getColumnName().getReference(),
                true,
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
        PersistentPropertyPathExtension path = new PersistentPropertyPathExtension(context, Objects.requireNonNull(persistentEntity));
        String reverseColumnName = property.getReverseColumnName(path).getReference();
        QEntityJoinColumnMetadata column = new QEntityJoinColumnMetadata(property.getActualType(),
                property.getName(),
                reverseColumnName,
                true,
                getIndex(property, fieldNameToIndex));
        metadata.addJoinColumn(column);
    }

    private Integer getIndex(RelationalPersistentProperty property, Map<String, Integer> fieldNameToIndex) {
        return fieldNameToIndex.get(property.getName());
    }
}
