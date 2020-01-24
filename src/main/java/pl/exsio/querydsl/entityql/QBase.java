package pl.exsio.querydsl.entityql;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.RelationalPathBase;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;
import pl.exsio.querydsl.entityql.path.QEnumPath;
import pl.exsio.querydsl.entityql.path.QUuidPath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Base QueryDSL Model class for EntityQL, contains all the protected methods EntityQL needs to properly work
 * from the original RelationalPathBase and all base dynamic methods shared between Q and QStaticModel classes
 *
 */
public abstract class QBase<E> extends RelationalPathBase<E> {

    protected final Map<String, Path<?>> columnsMap = new HashMap<>();

    protected final Map<String, ForeignKey<?>> joinColumnsMap = new HashMap<>();

    protected final Map<String, ForeignKey<?>> inverseJoinColumnsMap = new HashMap<>();

    QBase(Class<? extends E> type, String variable, String schema, String table) {
        super(type, variable, schema, table);
    }

    protected <C extends StoreClause<C>> StoreClause<C> set(StoreClause<C> clause, Function<Object, Path<Object>> pathProvider, Object... params) {
        if (params.length % 2 != 0) {
            throw new InvalidArgumentException("Odd number of parameters");
        }
        for (int i = 0; i < params.length - 1; i += 2) {
            clause.set(pathProvider.apply(params[i]), params[i + 1]);
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
            expressions.add(columns().get(column));
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
        return (QEnumPath<T>) columns().get(fieldName);
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
        return (QUuidPath) columns().get(fieldName);
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
        return (NumberPath<Long>) columns().get(fieldName);
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
        return (NumberPath<Float>) columns().get(fieldName);
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
        return (NumberPath<Integer>) columns().get(fieldName);
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
        return (NumberPath<Double>) columns().get(fieldName);
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
        return (NumberPath<Byte>) columns().get(fieldName);
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
        return (NumberPath<Short>) columns().get(fieldName);
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
        return (NumberPath<BigDecimal>) columns().get(fieldName);
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
        return (NumberPath<BigInteger>) columns().get(fieldName);
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
        return (NumberPath<T>) columns().get(fieldName);
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
        return (StringPath) columns().get(fieldName);
    }

    /**
     * Returns BooleanPath Expression for given Column Field name
     *
     * @throws InvalidArgumentException if the Column Field of that name doesn't exist in the current Model
     * @throws ClassCastException if the resulting Expression cannot be casted to BooleanPath
     * @param fieldName - entity Field name
     * @return - corresponding StringPath expressions
     */
    public BooleanPath bool(String fieldName) {
        checkIfColumnExists(fieldName);
        return (BooleanPath) columns().get(fieldName);
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
        return (DatePath<LocalDate>) columns().get(fieldName);
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
        return (ArrayPath<A, AE>) columns().get(fieldName);
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
        return (DateTimePath<LocalDateTime>) columns().get(fieldName);
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
        return (ComparableExpressionBase<T>) columns().get(fieldName);
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
        return (Path<T>) columns().get(fieldName);
    }

    private void checkIfColumnExists(String fieldName) {
        if (!containsColumn(fieldName)) {
            throw new InvalidArgumentException(String.format("No Column with name %s, available columns: %s",
                    fieldName, columns().keySet())
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
        if (!containsJoinColumn(fieldName)) {
            throw new InvalidArgumentException(String.format("No FK with name %s, available FKs: %s",
                    fieldName, joinColumns().keySet())
            );
        }
        return (ForeignKey<T>) joinColumns().get(fieldName);
    }

    /**
     * Returns ForeignKey<T> Expression for given inverse JoinColumn Field name
     *
     * @throws InvalidArgumentException if the inverse JoinColumn Field of that name doesn't exist in the current Model
     * @param fieldName - entity Field name
     * @return - corresponding ForeignKey<T> expressions
     */
    @SuppressWarnings(value = "unchecked")
    public <T> ForeignKey<T> inverseJoinColumn(String fieldName) {
        if (!containsInverseJoinColumn(fieldName)) {
            throw new InvalidArgumentException(String.format("No inverse FK with name %s, available FKs: %s",
                    fieldName, inverseJoinColumns().keySet())
            );
        }
        return (ForeignKey<T>) inverseJoinColumns().get(fieldName);
    }

    /**
     *
     * @return Map of All Columns with their respective Java Names as keys
     */
    public Map<String, Path<?>> columns() {
        return columnsMap;
    }

    /**
     *
     * @return Map of All JOin Columns with their respective Java Names as keys
     */
    public Map<String, ForeignKey<?>> joinColumns() {
        return joinColumnsMap;
    }

    /**
     *
     * @return Map of All inverse Join Columns with their respective Java Names as keys
     */
    public Map<String, ForeignKey<?>> inverseJoinColumns() {
        return joinColumnsMap;
    }

    /**
     *
     * @param fieldName - entity Field name
     * @return - true if current model contains a Column corresponding to the fieldName
     */
    public boolean containsColumn(String fieldName) {
        return columnsMap.containsKey(fieldName);
    }

    /**
     *
     * @param fieldName - entity Field name
     * @return - true if current Model contains a JoinColumn corresponding to the fieldName
     */
    public boolean containsJoinColumn(String fieldName) {
        return joinColumnsMap.containsKey(fieldName);
    }

    /**
     *
     * @param fieldName - entity Field name
     * @return - true if current Model contains an inverse JoinColumn corresponding to the fieldName
     */
    public boolean containsInverseJoinColumn(String fieldName) {
        return inverseJoinColumnsMap.containsKey(fieldName);
    }

    @Override
    protected <A, E1> ArrayPath<A, E1> createArray(String property, Class<? super A> type) {
        return super.createArray(property, type);
    }

    @Override
    protected BooleanPath createBoolean(String property) {
        return super.createBoolean(property);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <A extends Number & Comparable<?>> NumberPath<A> createNumber(String property, Class<? super A> type) {
        return super.createNumber(property, (Class<A>)type);
    }

    @Override
    protected StringPath createString(String property) {
        return super.createString(property);
    }

    @Override
    protected <A extends Comparable> DatePath<A> createDate(String property, Class<? super A> type) {
        return super.createDate(property, type);
    }

    @Override
    protected <A extends Comparable> DateTimePath<A> createDateTime(String property, Class<? super A> type) {
        return super.createDateTime(property, type);
    }

    @Override
    protected <A> SimplePath<A> createSimple(String property, Class<? super A> type) {
        return super.createSimple(property, type);
    }

    @Override
    protected <A extends Comparable> TimePath<A> createTime(String property, Class<? super A> type) {
        return super.createTime(property, type);
    }
}
