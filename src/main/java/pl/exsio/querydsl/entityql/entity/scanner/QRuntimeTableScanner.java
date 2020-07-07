package pl.exsio.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityMetadata;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.QRuntimeColumn;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.QRuntimeNamingStrategy;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.QRuntimeTable;
import pl.exsio.querydsl.entityql.ex.MissingIdException;

import java.util.List;

/**
 * QObjectScanner implementation based on Runtime Tables.
 */
public class QRuntimeTableScanner implements QObjectScanner<QRuntimeTable> {

    private final QRuntimeNamingStrategy namingStrategy;

    public QRuntimeTableScanner(QRuntimeNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    @Override
    public QEntityMetadata scanEntity(QRuntimeTable source) {
        QEntityMetadata metadata = new QEntityMetadata(source.getTableName(), source.getSchemaName());
        setColumns(metadata, source);

        return metadata;
    }

    private void setColumns(QEntityMetadata metadata, QRuntimeTable source) {
        List<QRuntimeColumn> columns = source.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            QRuntimeColumn column = columns.get(i);
            addColumn(metadata, column, i);
        }

        if (metadata.getIdColumns().isEmpty()) {
            throw new MissingIdException(source.getTableName());
        }
    }

    private void addColumn(QEntityMetadata metadata,
                           QRuntimeColumn column,
                           int index) {

        QEntityColumnMetadata columnMetadata = new QEntityColumnMetadata(
                column.getType(),
                namingStrategy.getFieldName(column.getColumnName()),
                column.getColumnName(),
                column.isNullable(),
                index);

        metadata.addColumn(columnMetadata);

        if (column.isPrimaryKey()) {
            metadata.addIdColumn(columnMetadata);
        }
    }

}
