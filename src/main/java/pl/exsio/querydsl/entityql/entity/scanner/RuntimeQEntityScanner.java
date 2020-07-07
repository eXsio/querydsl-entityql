package pl.exsio.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityMetadata;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.Column;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.NamingStrategy;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.Table;
import pl.exsio.querydsl.entityql.ex.MissingIdException;

import java.util.List;

/**
 * QEntityScanner implementation based on Spring Data JDBC.
 */
public class RuntimeQEntityScanner implements TableScanner {

    private final NamingStrategy namingStrategy;

    public RuntimeQEntityScanner(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    @Override
    public QEntityMetadata scanEntity(Table source) {
        QEntityMetadata metadata = new QEntityMetadata(source.getTableName(), source.getSchemaName());
        setColumns(metadata, source);

        return metadata;
    }

    private void setColumns(QEntityMetadata metadata, Table source) {
        List<Column> columns = source.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            addColumn(metadata, column, i);
        }

        if (metadata.getIdColumns().isEmpty()) {
            throw new MissingIdException(source.getTableName());
        }
    }

    private void addColumn(QEntityMetadata metadata,
                           Column column,
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
