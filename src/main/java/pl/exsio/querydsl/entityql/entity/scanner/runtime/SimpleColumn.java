package pl.exsio.querydsl.entityql.entity.scanner.runtime;

import java.util.Objects;

public class SimpleColumn implements Column {

    private Class<?> type;
    private String columnName;
    private boolean nullable;
    private boolean primaryKey;

    public SimpleColumn() {
    }

    public SimpleColumn(Class<?> type, String columnName, boolean nullable, boolean primaryKey) {
        this.type = type;
        this.columnName = columnName;
        this.nullable = nullable;
        this.primaryKey = primaryKey;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleColumn that = (SimpleColumn) o;
        return nullable == that.nullable &&
                primaryKey == that.primaryKey &&
                Objects.equals(type, that.type) &&
                Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, columnName, nullable, primaryKey);
    }

    @Override
    public String toString() {
        return columnName;
    }

}
