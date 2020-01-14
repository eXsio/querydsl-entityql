package pl.exsio.querydsl.entityql;

import com.querydsl.sql.ColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;

public abstract class QColumnMetadataFactory {

    static ColumnMetadata create(QEntityColumnMetadata column, int sqlType) {
        ColumnMetadata metadata = ColumnMetadata.named(column.getColumnName()).withIndex(column.getIdx()).ofType(sqlType);
        if (!column.isNullable()) {
            metadata = metadata.notNull();
        }
        return metadata;
    }

    public static ColumnMetadata create(String name, int idx, int sqlType, boolean nullable) {
        ColumnMetadata metadata = ColumnMetadata.named(name).withIndex(idx).ofType(sqlType);
        if (!nullable) {
            metadata = metadata.notNull();
        }
        return metadata;
    }
}
