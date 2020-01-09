package pl.exsio.querydsl.entityql;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.PrimaryKey;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;
import pl.exsio.querydsl.entityql.path.EnumPath;
import pl.exsio.querydsl.entityql.path.UuidPath;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Q<E> extends QBase<E> {

    private final Map<String, QPath> columns = new LinkedHashMap<>();

    private final Map<String, QForeignKey> joinColumns = new LinkedHashMap<>();

    private PrimaryKey<?> id;

    List<QColumnDefinition> idColumns = new LinkedList<>();

    private final Map<Path<?>, ColumnMetadata> columnMetadata = new LinkedHashMap<>();

    Q(Class<E> type, String variable, String schema, String table) {
        super(type, variable, schema, table);
    }

    void addColumn(Field field, Column column, int idx) {
        QColumn qColumn = new QColumn(this, field, column, idx);
        this.columns.put(field.getName(), qColumn.getPath());
        addMetadata(qColumn.getPath().get(), qColumn.getMetadata());
    }

    void addJoinColumn(Field field, JoinColumn column, int idx) {
        QJoinColumn qColumn = new QJoinColumn(this, field.getType(), column, idx);
        if (qColumn.getPaths().size() > 1) {
            throw new InvalidArgumentException(String.format("Single JoinColumn mapped to a Composite Primary Key: %s", field.getName()));
        }
        qColumn.getPaths().forEach((path, metadata) -> {
            this.columns.put(String.format("%sId", field.getName()), path);
            addMetadata(path.get(), metadata);
            ForeignKey<?> foreignKey = createForeignKey(path.get(), qColumn.getForeignColumnNames().getFirst());
            this.joinColumns.put(field.getName(), new QForeignKey(foreignKey, field, qColumn.getPaths(), qColumn.getForeignColumnNames()));
        });
    }

    void addJoinColumns(Field field, JoinColumns columns, Integer idx) {
        QJoinColumn qColumn = new QJoinColumn(this, field.getType(), columns, idx);
        qColumn.getPaths().forEach((path, metadata) -> addMetadata(path.get(), metadata));
        ForeignKey<?> foreignKey = createForeignKey(getPaths(qColumn), qColumn.getForeignColumnNames());
        this.joinColumns.put(field.getName(), new QForeignKey(foreignKey, field, qColumn.getPaths(), qColumn.getForeignColumnNames()));
    }

    private List<Path<?>> getPaths( QJoinColumn qColumn) {
        return qColumn.getPaths().keySet().stream()
                .map(QPath::get)
                .collect(Collectors.toList());
    }

    void addPrimaryKey(Map<Field, Column> ids) {
        idColumns = ids.entrySet().stream().map(e -> new QColumnDefinition(e.getKey(), e.getValue())).collect(Collectors.toList());
        List<String> pkColumnNames = ids.keySet().stream().map(Field::getName).collect(Collectors.toList());
        Path[] pkPaths = this.columns.entrySet().stream()
                .filter(e -> pkColumnNames.contains(e.getKey()))
                .map(e -> e.getValue().get())
                .toArray(Path[]::new);
        this.id = createPrimaryKey(pkPaths);
    }

    @Override
    protected <P extends Path<?>> P addMetadata(P path, ColumnMetadata metadata) {
        this.columnMetadata.put(path, metadata);
        return super.addMetadata(path, metadata);
    }

    @SuppressWarnings(value = "unchecked")
    public <C extends StoreClause<C>> StoreClause<C> set(StoreClause<C> clause, Object... params) {
        if (params.length % 2 != 0) {
            throw new InvalidArgumentException("Odd number of parameters");
        }
        for (int i = 0; i < params.length - 1; i += 2) {
            Object key = params[i];
            Object value = params[i + 1];
            if (!(key instanceof String)) {
                throw new InvalidArgumentException("Param key has to be String");
            }
            clause.set((Path<Object>) columns.get(key).get(), value);
        }
        return clause;
    }

    public List<Expression<?>> columns(String... columns) {
        List<Expression<?>> expressions = new LinkedList<>();
        for (String column : columns) {
            checkIfColumnExists(column);
            expressions.add(this.columns.get(column).get());
        }
        return expressions;
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends Enum<T>> EnumPath<T> enumerated(String fieldName) {
        checkIfColumnExists(fieldName);
        return (EnumPath<T>) this.columns.get(fieldName).get();
    }

    public UuidPath uuid(String fieldName) {
        checkIfColumnExists(fieldName);
        return (UuidPath) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public NumberPath<Long> longNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Long>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public NumberPath<Float> floatNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Float>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public NumberPath<Integer> intNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Integer>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public NumberPath<Double> doubleNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Double>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public NumberPath<Byte> byteNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Byte>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public NumberPath<Short> shortNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Short>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public NumberPath<BigDecimal> decimalNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<BigDecimal>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public NumberPath<BigInteger> bigIntNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<BigInteger>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends Number & Comparable<?>> NumberPath<T> number(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<T>) this.columns.get(fieldName).get();
    }

    public StringPath string(String fieldName) {
        checkIfColumnExists(fieldName);
        return (StringPath) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public DatePath<LocalDate> date(String fieldName) {
        checkIfColumnExists(fieldName);
        return (DatePath<LocalDate>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public <T> ArrayPath<T, T> array(String fieldName) {
        return (ArrayPath<T, T>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public DateTimePath<LocalDateTime> dateTime(String fieldName) {
        checkIfColumnExists(fieldName);
        return (DateTimePath<LocalDateTime>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends Comparable> ComparableExpressionBase<T> comparableColumn(String fieldName) {
        checkIfColumnExists(fieldName);
        return (ComparableExpressionBase<T>) this.columns.get(fieldName).get();
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends Comparable> Path<T> column(String fieldName) {
        checkIfColumnExists(fieldName);
        return (Path<T>) this.columns.get(fieldName).get();
    }

    private void checkIfColumnExists(String fieldName) {
        if (!columns.containsKey(fieldName)) {
            throw new InvalidArgumentException(String.format("No Column with name %s, available columns: %s", fieldName, columns.keySet()));
        }
    }

    @SuppressWarnings(value = "unchecked")
    public <T> ForeignKey<T> joinColumn(String fieldName) {
        if (!joinColumns.containsKey(fieldName)) {
            throw new InvalidArgumentException(String.format("No FK with name %s, available FKs: %s", fieldName, joinColumns.keySet()));
        }
        return (ForeignKey<T>) this.joinColumns.get(fieldName).get();
    }

    public boolean containsColumn(String fieldName) {
        return columns.containsKey(fieldName);
    }

    public boolean containsJoinColumn(String fieldName) {
        return joinColumns.containsKey(fieldName);
    }

    public Map<String, QPath> columns() {
        return columns;
    }

    public Map<String, QForeignKey> joinColumns() {
        return joinColumns;
    }

    @SuppressWarnings(value = "unchecked")
    public PrimaryKey<E> primaryKey() {
        return (PrimaryKey<E>) id;
    }

    Map<Path<?>, ColumnMetadata> getColumnMetadata() {
        return columnMetadata;
    }
}
