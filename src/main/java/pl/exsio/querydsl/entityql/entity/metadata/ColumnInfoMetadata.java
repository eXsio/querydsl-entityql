package pl.exsio.querydsl.entityql.entity.metadata;

public interface ColumnInfoMetadata {

    String getColumnName();

    boolean isNullable();

    String getColumnDefinition();
}
