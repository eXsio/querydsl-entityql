package pl.exsio.querydsl.entityql;

import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Map;

class QJoinColumn {

    private final Path<?> path;

    private final ColumnMetadata metadata;

    private final QColumnDefinition foreignColumn;

    QJoinColumn(Q<?> parent, Class<?> type, JoinColumn column, int idx) {
        foreignColumn = getForeignColumn(type, column);
        path = QPathFactory.create(parent, foreignColumn.getField(), column.name());
        ColumnMetadata metadata = ColumnMetadata
                .named(column.name())
                .withIndex(idx)
                .ofType(getSqlType(foreignColumn.getField(), column.columnDefinition()));
        if (!column.nullable()) {
            metadata = metadata.notNull();
        }
        this.metadata = metadata;
    }

    private QColumnDefinition getForeignColumn(Class<?> type, JoinColumn column) {
        Q<?> foreign = EntityQL.qEntityWithoutMappings(type);
        QColumnDefinition result = foreign.idColumn;
        if (isCustomForeignColumn(column)) {
            Map.Entry<Field, Map.Entry<Integer, Column>> foreignColumn = QFactory.get(type).getColumns()
                    .entrySet().stream()
                    .filter(e -> matchesCustomForeignColumnName(column, e))
                    .findFirst()
                    .orElseThrow(() ->
                            new InvalidArgumentException(String.format("Unable to find field mapped to Column '%s' in Entity %s", column.referencedColumnName(), type.getName()))
                    );
            result = new QColumnDefinition(foreignColumn.getKey(), foreignColumn.getValue().getValue());
        }
        return result;
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

    Path<?> getPath() {
        return path;
    }

    ColumnMetadata getMetadata() {
        return metadata;
    }

    String getForeignColumnName() {
        return foreignColumn.getColumn().name();
    }
}
