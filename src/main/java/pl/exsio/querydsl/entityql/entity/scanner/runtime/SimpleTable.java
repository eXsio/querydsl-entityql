package pl.exsio.querydsl.entityql.entity.scanner.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleTable implements Table {

    private String tableName;
    private String schemaName;
    private List<Column> columns = new ArrayList<>();

    public SimpleTable() {
    }

    public SimpleTable(String tableName, String schemaName, List<Column> columns) {
        this.tableName = tableName;
        this.schemaName = schemaName;
        this.columns = columns;
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
    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTable that = (SimpleTable) o;
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
