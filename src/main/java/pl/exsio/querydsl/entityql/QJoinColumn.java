package pl.exsio.querydsl.entityql;

import com.querydsl.sql.ColumnMetadata;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import java.lang.reflect.Field;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

class QJoinColumn {

    private final LinkedHashMap<QPath, ColumnMetadata> paths = new LinkedHashMap<>();

    private final List<QColumnDefinition> foreignColumns;

    QJoinColumn(Q<?> parent, Class<?> type, JoinColumn column, int idx) {
        foreignColumns = getForeignColumns(type, column);
        foreignColumns.forEach(foreignColumn -> createPath(parent, column, idx, foreignColumn));
    }

    QJoinColumn(Q<?> parent, Class<?> type, JoinColumns columns, int idx) {
        foreignColumns = getForeignColumns(type, columns);
        if(foreignColumns.size() != columns.value().length) {
            throw new InvalidArgumentException(String.format("Unable to construct Foreign Columns out of: %s", Arrays.toString(columns.value())));
        }
        for (int i = 0; i < foreignColumns.size(); i++) {
            createPath(parent, columns.value()[i], idx, foreignColumns.get(i));
        }
    }

    private void createPath(Q<?> parent, JoinColumn column, int idx, QColumnDefinition foreignColumn) {
        int sqlType = getSqlType(foreignColumn.getField(), column.columnDefinition());
        QPath qPath = QPathFactory.create(parent, foreignColumn.getField(), column.name(), column.nullable(), idx, sqlType);
        ColumnMetadata metadata = QColumnMetadataFactory.create(column.name(), idx, sqlType, column.nullable());
        paths.put(qPath, metadata);
    }

    private List<QColumnDefinition> getForeignColumns(Class<?> type, JoinColumn column) {
        Q<?> foreign = EntityQL.qEntityWithoutMappings(type);
        List<QColumnDefinition> result = foreign.idColumns;
        if (isCustomForeignColumn(column)) {
            result = new ArrayList<>();
            result.add(createCustomForeignColumn(type, column));
        }
        return result;
    }

    private List<QColumnDefinition> getForeignColumns(Class<?> type, JoinColumns columns) {
        final List<QColumnDefinition> result = new LinkedList<>();
        Arrays.stream(columns.value()).forEach(column -> {
            if (!isCustomForeignColumn(column)) {
               throw new InvalidArgumentException(String.format("Composite FK requires a non-empty referencedColumnName: %s", column.name()));
            }
            result.add(createCustomForeignColumn(type, column));
        });
        return result;
    }

    private QColumnDefinition createCustomForeignColumn(Class<?> type, JoinColumn column) {
        Map.Entry<Field, Map.Entry<Integer, Column>> foreignColumn = QFactory.get(type).getColumns()
                .entrySet().stream()
                .filter(e -> matchesCustomForeignColumnName(column, e))
                .findFirst()
                .orElseThrow(() ->
                        new InvalidArgumentException(String.format("Unable to find field mapped to Column '%s' in Entity %s", column.referencedColumnName(), type.getName()))
                );
        return new QColumnDefinition(foreignColumn.getKey(), foreignColumn.getValue().getValue());
    }

    private boolean matchesCustomForeignColumnName(JoinColumn column, Map.Entry<Field, Map.Entry<Integer, Column>> e) {
        return e.getValue().getValue().name().equals(column.referencedColumnName());
    }

    private boolean isCustomForeignColumn(JoinColumn column) {
        return !column.referencedColumnName().equals("");
    }

    private int getSqlType(Field field, String columnDefinition) {
        return QSqlTypeProvider.get(QField.getType(field)).map(t -> t.getSqlType(columnDefinition)).orElse(Types.OTHER);
    }

    LinkedHashMap<QPath, ColumnMetadata> getPaths() {
        return paths;
    }

    LinkedList<String> getForeignColumnNames() {
        return foreignColumns.stream()
                .map(fc -> fc.getColumn().name())
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
