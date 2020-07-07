package pl.exsio.querydsl.entityql.entity.scanner.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class SimpleQRuntimeTable implements QRuntimeTable {

    private String tableName;
    private String schemaName;
    private List<QRuntimeColumn> columns = new ArrayList<>();

    public SimpleQRuntimeTable(String tableName, String schemaName, List<QRuntimeColumn> columns) {
        this.tableName = tableName;
        this.schemaName = schemaName;
        this.columns = columns;
    }

    public SimpleQRuntimeTable(String tableName, String schemaName, QRuntimeColumn... columns) {
        this(tableName, schemaName, Arrays.asList(columns));
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public List<QRuntimeColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<QRuntimeColumn> columns) {
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleQRuntimeTable that = (SimpleQRuntimeTable) o;
        return Objects.equals(tableName, that.tableName) &&
                Objects.equals(schemaName, that.schemaName) &&
                Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, schemaName, columns);
    }

    @Override
    public String toString() {
        return tableName;
    }

}
