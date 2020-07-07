package pl.exsio.querydsl.entityql.entity.scanner.runtime;

public interface QRuntimeNamingStrategy {

    String getFieldName(String columnName);

    String getColumnName(String fieldName);

}
