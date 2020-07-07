package pl.exsio.querydsl.entityql.entity.scanner.runtime;

public interface NamingStrategy {

    String getFieldName(String columnName);

    String getColumnName(String fieldName);

}
