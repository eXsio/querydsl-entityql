package pl.exsio.querydsl.entityql.entity.metadata;

import java.lang.reflect.Array;

/**
 * Column Metadata of Java Field - DB Column mapping
 */
public class QEntityColumnMetadata implements ColumnInfoMetadata {

    /**
     * Original Class of the Java Field, in most cases read using Reflection
     */
    private final Class<?> originalFieldType;

    /**
     * Computed type is mostly the same as the original type.
     * Exceptions are Arrays and Enums. If a type returns true for isArray() or isEnum()
     * the computed class is changed to Array and Enum respectively
     */
    private final Class<?> computedFieldType;

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
     * Whether the DB Column is nullable or not
     */
    private final boolean nullable;

    /**
     * In most cases this value is not used. For now it is used only as an indicator
     * that a String should be treated as CLOB SQL Type. In such cases, use the value "CLOB".
     *
     * @since  2.2 this may also take a value of "BLOB" indicating that BLOB SQL Type should be used for ArrayPaths
     */
    private final String columnDefinition;

    /**
     * Index of the Field in Entity Class
     */
    private final int idx;


    public QEntityColumnMetadata(Class<?> originalFieldType,
                                 String fieldName, String columnName,
                                 boolean nullable, String columnDefinition, int idx) {
        this.originalFieldType = originalFieldType;
        this.computedFieldType = computeType(originalFieldType);
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.nullable = nullable;
        this.columnDefinition = columnDefinition;
        this.idx = idx;
    }

    public QEntityColumnMetadata(Class<?> originalFieldType,
                                 String fieldName, String columnName, boolean nullable, int idx) {
        this(originalFieldType, fieldName, columnName, nullable, "", idx);
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

    private Class<?> computeType(Class<?> type) {
        if (type.isArray()) {
            type = Array.class;
        } else if (type.isEnum()) {
            type = Enum.class;
        }
        return type;
    }
}
