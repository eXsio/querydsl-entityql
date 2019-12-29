package pl.exsio.querydsl.entityql;

import pl.exsio.querydsl.entityql.path.EnumPath;
import pl.exsio.querydsl.entityql.path.ObjectPath;
import pl.exsio.querydsl.entityql.path.UuidPath;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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

abstract class QPathFactory {

    private static final Map<Class<?>, PathFactory> pathFactory = new HashMap<>();

    static {
        pathFactory.put(Array.class, (q, field, name) -> new QPath(q.createArray(name, getType(field))));
        pathFactory.put(Long.class, (q, field, name) -> new QPath(q.createNumber(name, Long.class), Long.class));
        pathFactory.put(Float.class, (q, field, name) -> new QPath(q.createNumber(name, Float.class), Float.class));
        pathFactory.put(Double.class, (q, field, name) -> new QPath(q.createNumber(name, Double.class), Double.class));
        pathFactory.put(Integer.class, (q, field, name) -> new QPath(q.createNumber(name, Integer.class), Integer.class));
        pathFactory.put(Byte.class, (q, field, name) -> new QPath(q.createNumber(name, Byte.class), Byte.class));
        pathFactory.put(Short.class, (q, field, name) -> new QPath(q.createNumber(name, Short.class), Short.class));
        pathFactory.put(String.class, (q, field, name) -> new QPath(q.createString(name)));
        pathFactory.put(BigDecimal.class, (q, field, name) -> new QPath(q.createNumber(name, BigDecimal.class), BigDecimal.class));
        pathFactory.put(BigInteger.class, (q, field, name) -> new QPath(q.createNumber(name, BigInteger.class), BigInteger.class));
        pathFactory.put(LocalDate.class, (q, field, name) -> new QPath(q.createDate(name, LocalDate.class), LocalDate.class));
        pathFactory.put(LocalDateTime.class, (q, field, name) -> new QPath(q.createDateTime(name, LocalDateTime.class), LocalDateTime.class));
        pathFactory.put(Instant.class, (q, field, name) -> new QPath(q.createDateTime(name, Instant.class), Instant.class));
        pathFactory.put(LocalTime.class, (q, field, name) -> new QPath(q.createTime(name, LocalTime.class), LocalTime.class));
        pathFactory.put(Timestamp.class, (q, field, name) -> new QPath(q.createDateTime(name, Timestamp.class), Timestamp.class));
        pathFactory.put(Date.class, (q, field, name) -> new QPath(q.createDate(name, Date.class), Date.class));
        pathFactory.put(UUID.class, (q, field, name) -> new QPath(new UuidPath(q, name)));
        pathFactory.put(Enum.class, (q, field, name) -> new QPath(createEnumeratedPath(q, field, name), field.getType()));
        pathFactory.put(Object.class, (q, field, name) -> {
            Class<Object> type = getType(field);
            return new QPath(new ObjectPath<>(q.createSimple(name, type)), type);
        });
    }

    @SuppressWarnings(value = "unchecked")
    private static EnumPath<?> createEnumeratedPath(Q<?> q, Field field, String name) {
        return new EnumPath(field.getType(), q, name);
    }

    @SuppressWarnings(value = "unchecked")
    private static Class<Object> getType(Field field) {
        return (Class<Object>) field.getType();
    }

    static QPath create(Q q, Field field, String c) {
        Class<?> fieldType = QField.getType(field);
        if (!pathFactory.containsKey(fieldType)) {
            fieldType = Object.class;
        }
        return pathFactory.get(fieldType).createExpression(q, field, c);
    }

    private interface PathFactory {

        QPath createExpression(Q<?> q, Field field, String name);
    }
}
