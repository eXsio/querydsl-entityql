package pl.exsio.querydsl.entityql.entity.metadata;

/**
 * Metadata of DB Column that is a part of the Composite Foreign Key
 */
public class QEntityCompositeJoinColumnItemMetadata implements ReferenceColumnInfoMetadata {

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
     * For composite FKs this value is required as it is impossible to automatically
     * deduce the name of the Referenced Column Name
     */
    private final String referencedColumnName;

    /**
     * Whether the DB Column is nullable or not
     */
    private final boolean nullable;


    public QEntityCompositeJoinColumnItemMetadata(String columnName, String columnDefinition,
                                                  String referencedColumnName, boolean nullable) {
        this.columnName = columnName;
        this.columnDefinition = columnDefinition;
        this.referencedColumnName = referencedColumnName;
        this.nullable = nullable;
    }

    public QEntityCompositeJoinColumnItemMetadata(String columnName, String referencedColumnName, boolean nullable) {
        this(columnName, "", referencedColumnName, nullable);
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

    @Override
    public String getColumnDefinition() {
        return columnDefinition;
    }

    @Override
    public String toString() {
        return "QEntityCompositeJoinColumnItemMetadata{" +
                "columnName='" + columnName + '\'' +
                ", columnDefinition='" + columnDefinition + '\'' +
                ", referencedColumnName='" + referencedColumnName + '\'' +
                ", nullable=" + nullable +
                '}';
    }
}
