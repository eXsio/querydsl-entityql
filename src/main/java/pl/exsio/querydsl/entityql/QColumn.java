package pl.exsio.querydsl.entityql;

import com.querydsl.sql.ColumnMetadata;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.Types;

class QColumn {

    private final QPath path;

    private final ColumnMetadata metadata;

    QColumn(Q<?> parent, Field field, Column column, int idx) {
        int sqlType = getSqlType(field, column.columnDefinition());
        path = QPathFactory.create(parent, field, column.name(), column.nullable(), idx, sqlType);
        metadata = QColumnMetadataFactory.create(column.name(), idx, sqlType, column.nullable());
    }

    private int getSqlType(Field field, String columnDefinition) {
        return QSqlTypeProvider.get(QField.getType(field)).map(t -> t.getSqlType(columnDefinition)).orElse(Types.OTHER);
    }

    QPath getPath() {
        return path;
    }

    ColumnMetadata getMetadata() {
        return metadata;
    }
}
