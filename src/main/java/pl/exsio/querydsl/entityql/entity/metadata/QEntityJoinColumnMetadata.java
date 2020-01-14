package pl.exsio.querydsl.entityql.entity.metadata;

public class QEntityJoinColumnMetadata implements ReferenceColumnInfoMetadata {

    private final Class<?> fieldType;

    private final String fieldName;

    private final String columnName;

    private final String columnDefinition;

    private final String referencedColumnName;

    private final boolean nullable;

    private final int idx;

    public QEntityJoinColumnMetadata(Class<?> fieldType, String fieldName, String columnName, String columnDefinition,
                                     String referencedColumnName, boolean nullable, int idx) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.columnDefinition = columnDefinition;
        this.referencedColumnName = referencedColumnName;
        this.nullable = nullable;
        this.idx = idx;
    }


    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getReferencedColumnName() {
        return referencedColumnName;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    public int getIdx() {
        return idx;
    }

    @Override
    public String getColumnDefinition() {
        return columnDefinition;
    }
}
