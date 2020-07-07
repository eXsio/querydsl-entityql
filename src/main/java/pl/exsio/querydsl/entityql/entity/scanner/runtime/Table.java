package pl.exsio.querydsl.entityql.entity.scanner.runtime;

import java.util.List;

public interface Table {

    String getTableName();

    String getSchemaName();

    List<Column> getColumns();

    static Table of(String tableName, String schemaName, List<Column> columns) {
        return new SimpleTable(tableName, schemaName, columns);
    }

}
