package pl.exsio.querydsl.entityql;

import com.querydsl.sql.ColumnMetadata;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class QJoinColumn {

    private final LinkedList<QPath> paths = new LinkedList<>();

    private final LinkedList<ColumnMetadata> metadata = new LinkedList<>();

    private final List<QColumnDefinition> foreignColumns;

    QJoinColumn(Q<?> parent, Class<?> type, JoinColumn column, int idx) {
        foreignColumns = getForeignColumn(type, column);
        foreignColumns.forEach(foreignColumn -> {
            createPath(parent, column, idx, foreignColumn);
        });

    }

    private void createPath(Q<?> parent, JoinColumn column, int idx, QColumnDefinition foreignColumn) {
        paths.add(QPathFactory.create(parent, foreignColumn.getField(), column.name()));
        ColumnMetadata metadata = ColumnMetadata
                .named(column.name())
                .withIndex(idx)
                .ofType(getSqlType(foreignColumn.getField(), column.columnDefinition()));
        if (!column.nullable()) {
            metadata = metadata.notNull();
        }
        this.metadata.add(metadata);
    }

    private List<QColumnDefinition> getForeignColumn(Class<?> type, JoinColumn column) {
        Q<?> foreign = EntityQL.qEntityWithoutMappings(type);
        List<QColumnDefinition> result = foreign.idColumns;
        if (isCustomForeignColumn(column)) {
            result = new ArrayList<>();
            result.add(createCustomForeignColumn(type, column));
        }
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

    LinkedList<QPath> getPaths() {
        return paths;
    }

    LinkedList<ColumnMetadata> getMetadata() {
        return metadata;
    }

    List<String> getForeignColumnNames() {
        return foreignColumns.stream().map(fc -> fc.getColumn().name()).collect(Collectors.toList());
    }
}
