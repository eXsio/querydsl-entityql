package pl.exsio.querydsl.entityql;

import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Map;

class QColumn {

    private final Path<?> path;

    private final ColumnMetadata metadata;

    QColumn(Q<?> parent, Field field, Column column, int idx) {
        path = QPathFactory.create(parent, field, column.name());
        ColumnMetadata metadata = ColumnMetadata
                .named(column.name())
                .withIndex(idx)
                .ofType(getSqlType(field, column.columnDefinition()));
        if (!column.nullable()) {
            metadata = metadata.notNull();
        }
        this.metadata = metadata;
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
}
