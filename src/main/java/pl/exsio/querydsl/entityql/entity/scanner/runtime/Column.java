package pl.exsio.querydsl.entityql.entity.scanner.runtime;

public interface Column {

    Class<?> getType();

    String getColumnName();

    boolean isNullable();

    boolean isPrimaryKey();

    static Column of(Class<?> type, String columnName, boolean nullable, boolean primaryKey) {
        return new SimpleColumn(type, columnName, nullable, primaryKey);
    }

}
