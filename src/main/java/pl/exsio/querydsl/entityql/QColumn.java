package pl.exsio.querydsl.entityql;

import com.querydsl.sql.ColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;

import java.sql.Types;

class QColumn {

    private final QPath path;

    private final ColumnMetadata metadata;

    QColumn(Q<?> parent, QEntityColumnMetadata column) {
        int sqlType = getSqlType(column);
        path = QPathFactory.create(parent, column, sqlType);
        metadata = QColumnMetadataFactory.create(column, sqlType);
    }

    private int getSqlType(QEntityColumnMetadata column) {
        return QSqlTypeProvider.get(column.getComputedFieldType()).map(t -> t.getSqlType(column.getColumnDefinition())).orElse(Types.OTHER);
    }

    QPath getPath() {
        return path;
    }

    ColumnMetadata getMetadata() {
        return metadata;
    }
}
