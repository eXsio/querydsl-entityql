package pl.exsio.querydsl.entityql.entity.metadata;

import java.util.LinkedList;
import java.util.List;

/**
 * EntityQL's internal metadata format needed for creating dynamic and static EntityQL Models
 */
public class QEntityMetadata {

    /**
     * Table Name
     */
    private final String tableName;

    /**
     * Schema Name - can be set to an empty String if default Schema is used
     */
    private final String schemaName;

    /**
     * Column Metadata of Columns that serve as a Primary Key for the Table
     * In most cases the list will have only one item, but EntityQL handles
     * composite Primary Keys as well
     */
    private final List<QEntityColumnMetadata> idColumns = new LinkedList<>();

    /**
     * Column Metadata of all Columns (including Primary Keys) for the Table
     */
    private final List<QEntityColumnMetadata> columns = new LinkedList<>();

    /**
     * Column Metadata of all Join Columns (Foreign Keys) for the Table
     */
    private final List<QEntityJoinColumnMetadata> joinColumns = new LinkedList<>();

    /**
     * Column Metadata of all Composite Join Columns (Composite Foreign Keys) for the Table
     */
    private final List<QEntityCompositeJoinColumnMetadata> compositeJoinColumns = new LinkedList<>();

    /**
     * Column Metadata of all inverse Join Columns (Foreign Keys) for the Table
     */
    private final List<QEntityJoinColumnMetadata> inverseJoinColumns = new LinkedList<>();

    /**
     * Column Metadata of all inverse Composite Join Columns (Composite Foreign Keys) for the Table
     */
    private final List<QEntityCompositeJoinColumnMetadata> inverseCompositeJoinColumns = new LinkedList<>();

    public QEntityMetadata(String tableName, String schemaName) {
        this.tableName = tableName;
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public List<QEntityColumnMetadata> getColumns() {
        return columns;
    }

    public List<QEntityJoinColumnMetadata> getJoinColumns() {
        return joinColumns;
    }

    public List<QEntityCompositeJoinColumnMetadata> getCompositeJoinColumns() {
        return compositeJoinColumns;
    }

    public List<QEntityJoinColumnMetadata> getInverseJoinColumns() {
        return inverseJoinColumns;
    }

    public List<QEntityCompositeJoinColumnMetadata> getInverseCompositeJoinColumns() {
        return inverseCompositeJoinColumns;
    }

    public List<QEntityColumnMetadata> getIdColumns() {
        return idColumns;
    }

    public void addColumn(QEntityColumnMetadata column) {
        columns.add(column);
    }

    public void addJoinColumn(QEntityJoinColumnMetadata joinColumn) {
        joinColumns.add(joinColumn);
    }

    public void addCompositeJoinColumn(QEntityCompositeJoinColumnMetadata joinColumn) {
        compositeJoinColumns.add(joinColumn);
    }

    public void addInverseJoinColumn(QEntityJoinColumnMetadata joinColumn) {
        inverseJoinColumns.add(joinColumn);
    }

    public void addInverseCompositeJoinColumn(QEntityCompositeJoinColumnMetadata joinColumn) {
        inverseCompositeJoinColumns.add(joinColumn);
    }

    public void addIdColumn(QEntityColumnMetadata column) {
        idColumns.add(column);
    }
}
