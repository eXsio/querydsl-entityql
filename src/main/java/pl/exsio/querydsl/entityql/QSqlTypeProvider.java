package pl.exsio.querydsl.entityql;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

abstract class QSqlTypeProvider {

    private static final Map<Class<?>, TypeProvider> sqlTypeProvider = new HashMap<>();

    static {
        sqlTypeProvider.put(BigInteger.class, (columnDefinition -> Types.BIGINT));
        sqlTypeProvider.put(Long.class, (columnDefinition -> Types.BIGINT));
        sqlTypeProvider.put(Integer.class, (columnDefinition -> Types.INTEGER));
        sqlTypeProvider.put(Short.class, (columnDefinition -> Types.SMALLINT));
        sqlTypeProvider.put(Byte.class, (columnDefinition -> Types.SMALLINT));
        sqlTypeProvider.put(String.class, (columnDefinition -> "CLOB".equals(columnDefinition) ? Types.CLOB : Types.VARCHAR));
        sqlTypeProvider.put(BigDecimal.class, (columnDefinition -> Types.DECIMAL));
        sqlTypeProvider.put(Float.class, (columnDefinition -> Types.DECIMAL));
        sqlTypeProvider.put(Double.class, (columnDefinition -> Types.DECIMAL));
        sqlTypeProvider.put(LocalDate.class, (columnDefinition -> Types.TIMESTAMP));
        sqlTypeProvider.put(LocalDateTime.class, (columnDefinition -> Types.TIMESTAMP));
        sqlTypeProvider.put(Instant.class, (columnDefinition -> Types.TIMESTAMP));
        sqlTypeProvider.put(LocalTime.class, (columnDefinition -> Types.TIMESTAMP));
        sqlTypeProvider.put(Timestamp.class, (columnDefinition -> Types.TIMESTAMP));
        sqlTypeProvider.put(Date.class, (columnDefinition -> Types.TIMESTAMP));
        sqlTypeProvider.put(UUID.class, (columnDefinition -> Types.VARCHAR));
        sqlTypeProvider.put(Enum.class, (columnDefinition -> Types.VARCHAR));
        sqlTypeProvider.put(Object.class, (columnDefinition -> Types.OTHER));
        sqlTypeProvider.put(Array.class, (columnDefinition -> Types.ARRAY));

    }

    static Optional<TypeProvider> get(Class<?> fieldClass) {
        return Optional.ofNullable(sqlTypeProvider.get(fieldClass));
    }

    interface TypeProvider {

        int getSqlType(String columnDefinition);
    }
}
