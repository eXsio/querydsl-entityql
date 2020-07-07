package pl.exsio.querydsl.entityql.entity.scanner.runtime;

import java.util.List;

public interface QRuntimeTable {

    String getTableName();

    String getSchemaName();

    List<QRuntimeColumn> getColumns();

    public static QRuntimeTable of(String tableName, String schemaName, List<QRuntimeColumn> columns) {
        return new SimpleQRuntimeTable(tableName, schemaName, columns);
    }

    public static QRuntimeTable of(String tableName, String schemaName, QRuntimeColumn... columns) {
        return new SimpleQRuntimeTable(tableName, schemaName, columns);
    }

}
