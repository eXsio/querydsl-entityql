package pl.exsio.querydsl.entityql.entity.metadata;

/**
 * JoinColumn Metadata of Java Field - DB Foreign Key mapping
 */
public class QEntityJoinColumnMetadata implements ReferenceColumnInfoMetadata {

    /**
     * Class of the Java Field, in most cases read using Reflection
     */
    private final Class<?> fieldType;

    /**
     * Java field name. This name will be used in dynamic and static EntityQL Models
     * as a Column Identifier used in Java code
     */
    private final String fieldName;

    /**
     * DB Object Column Name mapped to the Java Field
     */
    private final String columnName;

    /**
     * In most cases this value is not used. For now it is used only as an indicator
     * that a String should be treated as CLOB SQL Type. In such cases, use the value "CLOB"
     */
    private final String columnDefinition;

    /**
     * In most cases this field should have an empty value, because the Referenced Column Name will
     * be the Primary Key of the Referenced Model. It is possible however to set a custom Name as well.
     */
    private final String referencedColumnName;

    /**
     * Whether the DB Column is nullable or not
     */
    private final boolean nullable;

    /**
     * Index of the Field in Entity Class
     */
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

    public QEntityJoinColumnMetadata(Class<?> fieldType, String fieldName, String columnName, boolean nullable, int idx) {
        this(fieldType, fieldName, columnName, "", "", nullable, idx);
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
