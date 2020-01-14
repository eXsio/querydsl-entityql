package pl.exsio.querydsl.entityql.entity.metadata;

import java.util.LinkedList;
import java.util.List;

public class QEntityMetadata {

    private final String tableName;

    private final String schemaName;

    private final List<QEntityColumnMetadata> idColumns = new LinkedList<>();

    private final List<QEntityColumnMetadata> columns = new LinkedList<>();

    private final List<QEntityJoinColumnMetadata> joinColumns = new LinkedList<>();

    private final List<QEntityCompositeJoinColumnMetadata> compositeJoinColumns = new LinkedList<>();

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

    public void addIdColumn(QEntityColumnMetadata column) {
        idColumns.add(column);
    }
}
