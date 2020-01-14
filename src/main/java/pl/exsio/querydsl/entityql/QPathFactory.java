package pl.exsio.querydsl.entityql;

import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;
import pl.exsio.querydsl.entityql.path.QEnumPath;
import pl.exsio.querydsl.entityql.path.QObjectPath;
import pl.exsio.querydsl.entityql.path.QUuidPath;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class QPathFactory {

    private static final Map<Class<?>, PathFactory> pathFactory = new HashMap<>();


    static {
        pathFactory.put(Array.class, (q, config) -> new QPath(q.createArray(config.getName(), getOriginalType(config)), config));
        pathFactory.put(Long.class, (q, config) -> new QPath(q.createNumber(config.getName(), Long.class), config, Long.class));
        pathFactory.put(Float.class, (q, config) -> new QPath(q.createNumber(config.getName(), Float.class), config, Float.class));
        pathFactory.put(Double.class, (q, config) -> new QPath(q.createNumber(config.getName(), Double.class), config, Double.class));
        pathFactory.put(Integer.class, (q, config) -> new QPath(q.createNumber(config.getName(), Integer.class), config, Integer.class));
        pathFactory.put(Byte.class, (q, config) -> new QPath(q.createNumber(config.getName(), Byte.class), config, Byte.class));
        pathFactory.put(Short.class, (q, config) -> new QPath(q.createNumber(config.getName(), Short.class), config, Short.class));
        pathFactory.put(String.class, (q, config) -> new QPath(q.createString(config.getName()), config));
        pathFactory.put(BigDecimal.class, (q, config) -> new QPath(q.createNumber(config.getName(), BigDecimal.class), config, BigDecimal.class));
        pathFactory.put(BigInteger.class, (q, config) -> new QPath(q.createNumber(config.getName(), BigInteger.class), config, BigInteger.class));
        pathFactory.put(LocalDate.class, (q, config) -> new QPath(q.createDate(config.getName(), LocalDate.class), config, LocalDate.class));
        pathFactory.put(LocalDateTime.class, (q, config) -> new QPath(q.createDateTime(config.getName(), LocalDateTime.class), config, LocalDateTime.class));
        pathFactory.put(Instant.class, (q, config) -> new QPath(q.createDateTime(config.getName(), Instant.class), config, Instant.class));
        pathFactory.put(LocalTime.class, (q, config) -> new QPath(q.createTime(config.getName(), LocalTime.class), config, LocalTime.class));
        pathFactory.put(Timestamp.class, (q, config) -> new QPath(q.createDateTime(config.getName(), Timestamp.class), config, Timestamp.class));
        pathFactory.put(Date.class, (q, config) -> new QPath(q.createDate(config.getName(), Date.class), config, Date.class));
        pathFactory.put(UUID.class, (q, config) -> new QPath(new QUuidPath(q, config.getName()), config));
        pathFactory.put(Enum.class, (q, config) -> new QPath(createEnumeratedPath(q, config), config, config.getOriginalFieldType()));
        pathFactory.put(Object.class, (q, config) -> new QPath(new QObjectPath<>(q.createSimple(config.getName(), getType(config))), config, config.getOriginalFieldType()));
    }

    @SuppressWarnings(value = "unchecked")
    private static QEnumPath<?> createEnumeratedPath(QBase<?> q, QPathConfig config) {
        return new QEnumPath(config.getComputedFieldType(), q, config.getName());
    }

    @SuppressWarnings(value = "unchecked")
    private static Class<Object> getType(QPathConfig config) {
        return (Class<Object>) config.getComputedFieldType();
    }

    @SuppressWarnings(value = "unchecked")
    private static Class<Object> getOriginalType(QPathConfig config) {
        return (Class<Object>) config.getOriginalFieldType();
    }

    static QPath create(Q<?> q, QEntityColumnMetadata column, int sqlType) {
        Class<?> computedFieldType = column.getComputedFieldType();
        if (!pathFactory.containsKey(computedFieldType)) {
            computedFieldType = Object.class;
        }
        return pathFactory.get(computedFieldType)
                .createExpression(q, new QPathConfig(column.getOriginalFieldType(), computedFieldType,
                        column.getColumnName(), column.isNullable(), column.getIdx(), sqlType)
                );
    }

    @SuppressWarnings(value = "unchecked")
    public static <R> R create(QBase<?> q, QPathConfig config) {
        return (R) pathFactory.get(config.getComputedFieldType()).createExpression(q, config).get();
    }

    private interface PathFactory {

        QPath createExpression(QBase<?> q, QPathConfig config);
    }
}
