package pl.exsio.querydsl.entityql.entity.metadata;

public class QEntityColumnMetadata implements ColumnInfoMetadata {

    private final Class<?> originalFieldType;

    private final Class<?> computedFieldType;

    private final String fieldName;

    private final String columnName;

    private final boolean nullable;

    private final String columnDefinition;

    private final int idx;


    public QEntityColumnMetadata(Class<?> originalFieldType, Class<?> computedFieldType,
                                 String fieldName, String columnName,
                                 boolean nullable, String columnDefinition, int idx) {
        this.originalFieldType = originalFieldType;
        this.computedFieldType = computedFieldType;
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.nullable = nullable;
        this.columnDefinition = columnDefinition;
        this.idx = idx;
    }

    public Class<?> getOriginalFieldType() {
        return originalFieldType;
    }

    public Class<?> getComputedFieldType() {
        return computedFieldType;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    public int getIdx() {
        return idx;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getColumnDefinition() {
        return columnDefinition;
    }
}
