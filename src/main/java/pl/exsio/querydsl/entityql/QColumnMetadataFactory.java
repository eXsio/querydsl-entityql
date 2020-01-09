package pl.exsio.querydsl.entityql;

import com.querydsl.sql.ColumnMetadata;

public abstract class QColumnMetadataFactory {

    public static ColumnMetadata create(String name, int idx, int sqlType, boolean nullable) {
        ColumnMetadata metadata = ColumnMetadata.named(name).withIndex(idx).ofType(sqlType);
        if (!nullable) {
            metadata = metadata.notNull();
        }
        return metadata;
    }
}
