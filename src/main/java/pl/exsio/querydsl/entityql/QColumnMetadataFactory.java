package pl.exsio.querydsl.entityql;

import com.querydsl.sql.ColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;

/**
 * Factory Class to construct QueryDSL's ColumnMetadata instances
 */
public abstract class QColumnMetadataFactory {

    static ColumnMetadata create(QEntityColumnMetadata column, int sqlType) {
        ColumnMetadata metadata = ColumnMetadata
                .named(column.getColumnName())
                .withIndex(column.getIdx())
                .ofType(sqlType);
        if (!column.isNullable()) {
            metadata = metadata.notNull();
        }
        return metadata;
    }

    public static ColumnMetadata create(QPathConfig config) {
        ColumnMetadata metadata = ColumnMetadata
                .named(config.getName())
                .withIndex(config.getIdx())
                .ofType(config.getSqlType());
        if (!config.isNullable()) {
            metadata = metadata.notNull();
        }
        return metadata;
    }
}
