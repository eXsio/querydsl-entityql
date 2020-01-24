package pl.exsio.querydsl.entityql;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.PrimaryKey;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityCompositeJoinColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityJoinColumnMetadata;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;

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

    private final Map<String, QPath> rawColumns = new LinkedHashMap<>();

    private final Map<String, QForeignKey> rawJoinColumns = new LinkedHashMap<>();

    private final Map<String, QForeignKey> rawInverseJoinColumns = new LinkedHashMap<>();

    private PrimaryKey<?> id;

    List<QEntityColumnMetadata> idColumns = new LinkedList<>();

    Q(Class<E> type, String variable, String schema, String table, QEntityScanner scanner) {
        super(type, variable, schema, table);
        this.scanner = scanner;
    }

    void addColumn(QEntityColumnMetadata column) {
        QColumn qColumn = new QColumn(this, column);
        this.rawColumns.put(column.getFieldName(), qColumn.getPath());
        this.columnsMap.put(column.getFieldName(), qColumn.getPath().get());
        addMetadata(qColumn.getPath().get(), qColumn.getMetadata());
    }

    void addJoinColumn(QEntityJoinColumnMetadata column) {
        QJoinColumn qColumn = new QJoinColumn(this, column, scanner, false);
        if (qColumn.getPaths().size() > 1) {
            throw new InvalidArgumentException(String.format("Single JoinColumn mapped to a Composite Primary Key: %s",
                    column.getFieldName())
            );
        }
        qColumn.getPaths().forEach((path, metadata) -> {
            String idColumnName = String.format("%sId", column.getFieldName());
            this.rawColumns.put(idColumnName, path);
            this.columnsMap.put(idColumnName, path.get());
            addMetadata(path.get(), metadata);
            ForeignKey<?> foreignKey = createForeignKey(path.get(), qColumn.getForeignColumnNames().getFirst());
            this.rawJoinColumns.put(
                    column.getFieldName(),
                    new QForeignKey(foreignKey, column.getFieldType(), qColumn.getPaths(), qColumn.getForeignColumnNames())
            );
            this.joinColumnsMap.put(column.getFieldName(), foreignKey);
        });
    }

    void addInverseJoinColumn(QEntityJoinColumnMetadata column) {
        QJoinColumn qColumn = new QJoinColumn(this, column, scanner, true);
        if (qColumn.getPaths().size() > 1) {
            throw new InvalidArgumentException(String.format("Single inverse JoinColumn mapped to a Composite Primary Key: %s",
                    column.getFieldName())
            );
        }
        qColumn.getPaths().forEach((path, metadata) -> {
            addMetadata(path.get(), metadata);
            ForeignKey<?> foreignKey = createInvForeignKey(path.get(), qColumn.getForeignColumnNames().getFirst());
            this.rawInverseJoinColumns.put(
                    column.getFieldName(),
                    new QForeignKey(foreignKey, column.getFieldType(), qColumn.getPaths(), qColumn.getForeignColumnNames())
            );
            this.inverseJoinColumnsMap.put(column.getFieldName(), foreignKey);
        });
    }

    void addCompositeJoinColumn(QEntityCompositeJoinColumnMetadata column) {
        QJoinColumn qColumn = new QJoinColumn(this, column, scanner, false);
        qColumn.getPaths().forEach((path, metadata) -> addMetadata(path.get(), metadata));
        ForeignKey<?> foreignKey = createForeignKey(getPaths(qColumn), qColumn.getForeignColumnNames());
        this.rawJoinColumns.put(
                column.getFieldName(),
                new QForeignKey(foreignKey, column.getFieldType(), qColumn.getPaths(), qColumn.getForeignColumnNames())
        );
        this.joinColumnsMap.put(column.getFieldName(), foreignKey);
    }

    void addInverseCompositeJoinColumn(QEntityCompositeJoinColumnMetadata column) {
        QJoinColumn qColumn = new QJoinColumn(this, column, scanner, true);
        qColumn.getPaths().forEach((path, metadata) -> addMetadata(path.get(), metadata));
        ForeignKey<?> foreignKey = createInvForeignKey(getPaths(qColumn), qColumn.getForeignColumnNames());
        this.rawInverseJoinColumns.put(
                column.getFieldName(),
                new QForeignKey(foreignKey, column.getFieldType(), qColumn.getPaths(), qColumn.getForeignColumnNames())
        );
        this.inverseJoinColumnsMap.put(column.getFieldName(), foreignKey);
    }

    private List<Path<?>> getPaths( QJoinColumn qColumn) {
        return qColumn.getPaths().keySet().stream()
                .map(QPath::get)
                .collect(Collectors.toList());
    }

    void addPrimaryKey(List<QEntityColumnMetadata> ids) {
        idColumns = ids;
        List<String> pkColumnNames = ids.stream().map(QEntityColumnMetadata::getFieldName).collect(Collectors.toList());
        Path[] pkPaths = this.rawColumns.entrySet().stream()
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
        return super.set(clause, key -> {
            if (!(key instanceof String)) {
                throw new InvalidArgumentException("Param key has to be String");
            }
            return  (Path<Object>) rawColumns.get(key).get();
        }, params);
    }

    /**
     *
     * @return Map of all raw Columns for current Model
     */
    public Map<String, QPath> rawColumns() {
        return rawColumns;
    }

    /**
     *
     * @return Map of all raw JoinColumns for current Model
     */
    public Map<String, QForeignKey> rawJoinColumns() {
        return rawJoinColumns;
    }

    /**
     *
     * @return Map of all raw inverse JoinColumns for current Model
     */
    public Map<String, QForeignKey> rawInverseJoinColumns() {
        return rawInverseJoinColumns;
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