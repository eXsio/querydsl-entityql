package pl.exsio.querydsl.entityql;

public class QPathConfig {

    private final Class<?> originalFieldType;

    private final Class<?> fieldType;

    private final String name;

    private final boolean nullable;

    private final int idx;

    private final int sqlType;

    public QPathConfig(Class<?> originalFieldType, Class<?> fieldType, String name, boolean nullable, int idx, int sqlType) {
        this.originalFieldType = originalFieldType;
        this.fieldType = fieldType;
        this.name = name;
        this.nullable = nullable;
        this.idx = idx;
        this.sqlType = sqlType;
    }

    public Class<?> getOriginalFieldType() {
        return originalFieldType;
    }

    public Class<?> getFieldType() {
        return fieldType;
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
