package pl.exsio.querydsl.entityql.entity.scanner.runtime;

public interface QRuntimeColumn {

    Class<?> getType();

    String getColumnName();

    boolean isNullable();

    boolean isPrimaryKey();

    public static QRuntimeColumn of(Class<?> type, String columnName, boolean nullable, boolean primaryKey) {
        return new SimpleQRuntimeColumn(type, columnName, nullable, primaryKey);
    }

}
