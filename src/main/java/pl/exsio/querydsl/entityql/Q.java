package pl.exsio.querydsl.entityql;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.PrimaryKey;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityCompositeJoinColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityJoinColumnMetadata;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;
import pl.exsio.querydsl.entityql.path.QEnumPath;
import pl.exsio.querydsl.entityql.path.QUuidPath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main EntityQL class, represents a Dynamic QueryDSL Model. Contains java.util.Map collections containing
 * DSL Expressions for Columns and Join Columns.  Used to construct QueryDSL queries and to export the
 * metadata to Static Java classes using the QExporter utility.
 *
 *
 * @param <E> - Entity Class
 */
public class Q<E> extends QBase<E> {

    private final QEntityScanner scanner;

    private final Map<String, QPath> columns = new LinkedHashMap<>();

    private final Map<String, QForeignKey> joinColumns = new LinkedHashMap<>();

    private PrimaryKey<?> id;

    List<QEntityColumnMetadata> idColumns = new LinkedList<>();

    Q(Class<E> type, String variable, String schema, String table, QEntityScanner scanner) {
        super(type, variable, schema, table);
        this.scanner = scanner;
    }

    void addColumn(QEntityColumnMetadata column) {
        QColumn qColumn = new QColumn(this, column);
        this.columns.put(column.getFieldName(), qColumn.getPath());
        addMetadata(qColumn.getPath().get(), qColumn.getMetadata());
    }

    void addJoinColumn(QEntityJoinColumnMetadata column) {
        QJoinColumn qColumn = new QJoinColumn(this, column, scanner);
        if (qColumn.getPaths().size() > 1) {
            throw new InvalidArgumentException(String.format("Single JoinColumn mapped to a Composite Primary Key: %s",
                    column.getFieldName())
            );
        }
        qColumn.getPaths().forEach((path, metadata) -> {
            this.columns.put(String.format("%sId", column.getFieldName()), path);
            addMetadata(path.get(), metadata);
            ForeignKey<?> foreignKey = createForeignKey(path.get(), qColumn.getForeignColumnNames().getFirst());
            this.joinColumns.put(
                    column.getFieldName(),
                    new QForeignKey(foreignKey, column.getFieldType(), qColumn.getPaths(), qColumn.getForeignColumnNames())
            );
        });
    }

    void addCompositeJoinColumn(QEntityCompositeJoinColumnMetadata column) {
        QJoinColumn qColumn = new QJoinColumn(this, column, scanner);
        qColumn.getPaths().forEach((path, metadata) -> addMetadata(path.get(), metadata));
        ForeignKey<?> foreignKey = createForeignKey(getPaths(qColumn), qColumn.getForeignColumnNames());
        this.joinColumns.put(
                column.getFieldName(),
                new QForeignKey(foreignKey, column.getFieldType(), qColumn.getPaths(), qColumn.getForeignColumnNames())
        );
    }

    private List<Path<?>> getPaths( QJoinColumn qColumn) {
        return qColumn.getPaths().keySet().stream()
                .map(QPath::get)
                .collect(Collectors.toList());
    }

    void addPrimaryKey(List<QEntityColumnMetadata> ids) {
        idColumns = ids;
        List<String> pkColumnNames = ids.stream().map(QEntityColumnMetadata::getFieldName).collect(Collectors.toList());
        Path[] pkPaths = this.columns.entrySet().stream()
                .filter(e -> pkColumnNames.contains(e.getKey()))
                .map(e -> e.getValue().get())
                .toArray(Path[]::new);
        this.id = createPrimaryKey(pkPaths);
    }

    @Override
    protected <P extends Path<?>> P addMetadata(P path, ColumnMetadata metadata) {
        return super.addMetadata(path, metadata);
    }

    /**
     * Convenience method used to quiclky set values on insert/update/merge clauses.
     * Method requires the 'params' parameter to be of even size.
     * Every other params array item has to be a String
     *
     * Example:
     *
     * Q<Book> book = qEntity(Book);
     * book.set(
     *     queryFactory.insert(book),
     *     "id", 11L,
     *     "name", "newBook2",
     *     "price", BigDecimal.ONE
     * ).execute();
     *
     * @param clause - insert/update/merge clause
     * @param params - varargs array with parameters
     * @return clause from the 1st argument
     */
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

    /**
     * Convenience method used for getting a list of column expressions
     * that belong to this Model instance.
     *
     * Example usage:
     *
     * List<Book> books = queryFactory.query()
     *                 .select(
     *                         dto(Book.class, book.columns("id", "name", "desc", "price"))
     *                 ).fetch();
     *
     * @param columns - column names
     * @return - list of corresponding QueryDSL Expressions
     */
    public List<Expression<?>> columns(String... columns) {
        List<Expression<?>> expressions = new LinkedList<>();
        for (String column : columns) {
            checkIfColumnExists(column);
            expressions.add(this.columns.get(column).get());
        }
        return expressions;
    }

    /**
     * Returns QEnumPath Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to QEnumPath
     * @param fieldName - entity Field name
     * @return - corresponding QEnumPath expressions
     */
    @SuppressWarnings(value = "unchecked")
    public <T extends Enum<T>> QEnumPath<T> enumerated(String fieldName) {
        checkIfColumnExists(fieldName);
        return (QEnumPath<T>) this.columns.get(fieldName).get();
    }

    /**
     * Returns QUuidPath Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to QUuidPath
     * @param fieldName - entity Field name
     * @return - corresponding QUuidPath expressions
     */
    public QUuidPath uuid(String fieldName) {
        checkIfColumnExists(fieldName);
        return (QUuidPath) this.columns.get(fieldName).get();
    }

    /**
     * Returns NumberPath<Long> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to NumberPath
     * @param fieldName - entity Field name
     * @return - corresponding NumberPath<Long> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public NumberPath<Long> longNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Long>) this.columns.get(fieldName).get();
    }

    /**
     * Returns NumberPath<Float> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to NumberPath
     * @param fieldName - entity Field name
     * @return - corresponding NumberPath<Float> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public NumberPath<Float> floatNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Float>) this.columns.get(fieldName).get();
    }

    /**
     * Returns NumberPath<Integer> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to NumberPath
     * @param fieldName - entity Field name
     * @return - corresponding NumberPath<Integer> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public NumberPath<Integer> intNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Integer>) this.columns.get(fieldName).get();
    }

    /**
     * Returns NumberPath<Double> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to NumberPath
     * @param fieldName - entity Field name
     * @return - corresponding NumberPath<Double> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public NumberPath<Double> doubleNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Double>) this.columns.get(fieldName).get();
    }

    /**
     * Returns NumberPath<Byte> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to NumberPath
     * @param fieldName - entity Field name
     * @return - corresponding NumberPath<Byte> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public NumberPath<Byte> byteNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Byte>) this.columns.get(fieldName).get();
    }

    /**
     * Returns NumberPath<Short> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to NumberPath
     * @param fieldName - entity Field name
     * @return - corresponding NumberPath<Short> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public NumberPath<Short> shortNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<Short>) this.columns.get(fieldName).get();
    }

    /**
     * Returns NumberPath<BigDecimal> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to NumberPath
     * @param fieldName - entity Field name
     * @return - corresponding NumberPath<BigDecimal> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public NumberPath<BigDecimal> decimalNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<BigDecimal>) this.columns.get(fieldName).get();
    }

    /**
     * Returns NumberPath<BigInteger> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to NumberPath
     * @param fieldName - entity Field name
     * @return - corresponding NumberPath<BigInteger> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public NumberPath<BigInteger> bigIntNumber(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<BigInteger>) this.columns.get(fieldName).get();
    }

    /**
     * Returns NumberPath<T> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to NumberPath
     * @param fieldName - entity Field name
     * @return - corresponding NumberPath<T> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public <T extends Number & Comparable<?>> NumberPath<T> number(String fieldName) {
        checkIfColumnExists(fieldName);
        return (NumberPath<T>) this.columns.get(fieldName).get();
    }

    /**
     * Returns StringPath Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to StringPath
     * @param fieldName - entity Field name
     * @return - corresponding StringPath expressions
     */
    public StringPath string(String fieldName) {
        checkIfColumnExists(fieldName);
        return (StringPath) this.columns.get(fieldName).get();
    }

    /**
     * Returns DatePath<LocalDate> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to DatePath
     * @param fieldName - entity Field name
     * @return - corresponding DatePath<LocalDate> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public DatePath<LocalDate> date(String fieldName) {
        checkIfColumnExists(fieldName);
        return (DatePath<LocalDate>) this.columns.get(fieldName).get();
    }

    /**
     * Returns ArrayPath<A, AE> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to ArrayPath
     * @param fieldName - entity Field name
     * @param <A> - Array Type (for example byte[])
     * @param <AE> - Array Element Type (for example Byte)
     *
     * @return - corresponding ArrayPath<A, AE> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public <A, AE> ArrayPath<A, AE> array(String fieldName) {
        return (ArrayPath<A, AE>) this.columns.get(fieldName).get();
    }

    /**
     * Returns DateTimePath<LocalDateTime> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to DateTimePath
     * @param fieldName - entity Field name
     * @return - corresponding DateTimePath<LocalDateTime> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public DateTimePath<LocalDateTime> dateTime(String fieldName) {
        checkIfColumnExists(fieldName);
        return (DateTimePath<LocalDateTime>) this.columns.get(fieldName).get();
    }

    /**
     * Returns ComparableExpressionBase<T> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to ComparableExpressionBase
     * @param fieldName - entity Field name
     * @return - corresponding ComparableExpressionBase<T> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public <T extends Comparable> ComparableExpressionBase<T> comparableColumn(String fieldName) {
        checkIfColumnExists(fieldName);
        return (ComparableExpressionBase<T>) this.columns.get(fieldName).get();
    }

    /**
     * Returns Path<T> Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to Path<T>
     * @param fieldName - entity Field name
     * @return - corresponding Path<T> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public <T extends Comparable> Path<T> column(String fieldName) {
        checkIfColumnExists(fieldName);
        return (Path<T>) this.columns.get(fieldName).get();
    }

    private void checkIfColumnExists(String fieldName) {
        if (!columns.containsKey(fieldName)) {
            throw new InvalidArgumentException(String.format("No Column with name %s, available columns: %s",
                    fieldName, columns.keySet())
            );
        }
    }

    /**
     * Returns ForeignKey<T> Expression for given JoinColumn Field name
     *
     * @throws InvalidArgumentException if the JoinColumn Field of that name doesn't exist in the current Model
     * @param fieldName - entity Field name
     * @return - corresponding ForeignKey<T> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public <T> ForeignKey<T> joinColumn(String fieldName) {
        if (!joinColumns.containsKey(fieldName)) {
            throw new InvalidArgumentException(String.format("No FK with name %s, available FKs: %s",
                    fieldName, joinColumns.keySet())
            );
        }
        return (ForeignKey<T>) this.joinColumns.get(fieldName).get();
    }

    /**
     *
     * @param fieldName - entity Field name
     * @return - true if current model contains a Column corresponding to the fieldName
     */
    public boolean containsColumn(String fieldName) {
        return columns.containsKey(fieldName);
    }

    /**
     *
     * @param fieldName - entity Field name
     * @return - true if current Model contains a JoinColumn corresponding to the fieldName
     */
    public boolean containsJoinColumn(String fieldName) {
        return joinColumns.containsKey(fieldName);
    }

    /**
     *
     * @return Map of all Columns for current Model
     */
    public Map<String, QPath> columns() {
        return columns;
    }

    /**
     *
     * @return Map of all JoinColumns for current Model
     */
    public Map<String, QForeignKey> joinColumns() {
        return joinColumns;
    }

    /**
     *
     * @return QueryDSL's PrimaryKey Expression for given Model
     */
    @SuppressWarnings(value = "unchecked")
    public PrimaryKey<E> primaryKey() {
        return (PrimaryKey<E>) id;
    }
}