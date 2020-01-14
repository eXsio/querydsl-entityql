package pl.exsio.querydsl.entityql;

public class QPathConfig {

    private final Class<?> originalFieldType;

    private final Class<?> computedFieldType;

    private final String name;

    private final boolean nullable;

    private final int idx;

    private final int sqlType;

    public QPathConfig(Class<?> originalFieldType, Class<?> computedFieldType, String name,
                       boolean nullable, int idx, int sqlType) {
        this.originalFieldType = originalFieldType;
        this.computedFieldType = computedFieldType;
        this.name = name;
        this.nullable = nullable;
        this.idx = idx;
        this.sqlType = sqlType;
    }

    public Class<?> getOriginalFieldType() {
        return originalFieldType;
    }

    public Class<?> getComputedFieldType() {
        return computedFieldType;
    }

    public String getName() {
        return name;
    }

    public boolean isNullable() {
        return nullable;
    }

    public int getIdx() {
        return idx;
    }

    public int getSqlType() {
        return sqlType;
    }
}
