package pl.exsio.querydsl.entityql;

import com.google.common.collect.Lists;
import com.querydsl.sql.ColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityCompositeJoinColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.QEntityJoinColumnMetadata;
import pl.exsio.querydsl.entityql.entity.metadata.ReferenceColumnInfoMetadata;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;

import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class QJoinColumn {

    private final LinkedHashMap<QPath, ColumnMetadata> paths = new LinkedHashMap<>();

    private final List<QEntityColumnMetadata> foreignColumns;

    private final QEntityScanner scanner;

    private final boolean inverse;

    private final LinkedList<String> columnNames = Lists.newLinkedList();

    private final Q<?> parent;

    QJoinColumn(Q<?> parent, QEntityJoinColumnMetadata column, QEntityScanner scanner, boolean inverse) {
        this.scanner = scanner;
        this.inverse = inverse;
        this.parent = parent;
        this.columnNames.add(column.getColumnName());
        foreignColumns = getForeignColumns(column);
        foreignColumns.forEach(foreignColumn -> createPath(parent, column.getFieldName(), column.getIdx(), column, foreignColumn));
    }

    QJoinColumn(Q<?> parent, QEntityCompositeJoinColumnMetadata column, QEntityScanner scanner, boolean inverse) {
        this.scanner = scanner;
        this.inverse = inverse;
        this.parent = parent;
        column.getItems().forEach(item -> this.columnNames.add(item.getColumnName()));
        foreignColumns = getForeignColumns(column);
        if (foreignColumns.size() != column.getItems().size()) {
            throw new InvalidArgumentException(String.format("Unable to construct Foreign Columns out of: %s", column.getItems()));
        }
        for (int i = 0; i < foreignColumns.size(); i++) {
            createPath(parent, column.getFieldName(), column.getIdx(), column.getItems().get(i), foreignColumns.get(i));
        }
    }

    private void createPath(Q<?> parent, String fieldName, int idx, ReferenceColumnInfoMetadata column, QEntityColumnMetadata foreignColumn) {
        QEntityColumnMetadata computedColumn = new QEntityColumnMetadata(foreignColumn.getOriginalFieldType(),
                fieldName, inverse ? foreignColumn.getColumnName() : column.getColumnName(), column.isNullable(), column.getColumnDefinition(), idx);

        int sqlType = getSqlType(computedColumn);
        QPath qPath = QPathFactory.create(parent, computedColumn, sqlType);
        ColumnMetadata metadata = QColumnMetadataFactory.create(computedColumn, sqlType);
        paths.put(qPath, metadata);
    }

    private List<QEntityColumnMetadata> getForeignColumns(QEntityJoinColumnMetadata column) {
        Q<?> foreign = inverse ?  EntityQL.qEntityWithoutMappings(parent.getType(), scanner) : EntityQL.qEntityWithoutMappings(column.getFieldType(), scanner);
        List<QEntityColumnMetadata> result = foreign.idColumns;
        if (isCustomForeignColumn(column)) {
            result = new ArrayList<>();
            result.add(createCustomForeignColumn(inverse ? parent.getType() : column.getFieldType(), column));
        }
        return result;
    }

    private List<QEntityColumnMetadata> getForeignColumns(QEntityCompositeJoinColumnMetadata column) {
        final List<QEntityColumnMetadata> result = new LinkedList<>();
        column.getItems().forEach(item -> {
            if (!isCustomForeignColumn(item)) {
                throw new InvalidArgumentException(String.format("Composite FK requires a non-empty referencedColumnName: %s",
                        item.getColumnName()));
            }
            result.add(createCustomForeignColumn(inverse ? parent.getType() : column.getFieldType(), item));
        });
        return result;
    }

    private QEntityColumnMetadata createCustomForeignColumn(Class<?> fieldType, ReferenceColumnInfoMetadata column) {
        return QFactory.get(fieldType, scanner).getMetadata()
                .getColumns().stream()
                .filter(fc -> matchesCustomForeignColumnName(column, fc))
                .findFirst()
                .orElseThrow(() ->
                        new InvalidArgumentException(String.format("Unable to find field mapped to Column '%s' in Entity %s",
                                column.getReferencedColumnName(), fieldType.getName()))
                );
    }

    private boolean matchesCustomForeignColumnName(ReferenceColumnInfoMetadata column, QEntityColumnMetadata foreignColumn) {
        return foreignColumn.getColumnName().equals(column.getReferencedColumnName());
    }

    private boolean isCustomForeignColumn(ReferenceColumnInfoMetadata column) {
        return !column.getReferencedColumnName().equals("");
    }

    private int getSqlType(QEntityColumnMetadata computedColumn) {
        return QSqlTypeProvider
                .get(computedColumn.getComputedFieldType())
                .map(t -> t.getSqlType(computedColumn.getColumnDefinition())).orElse(Types.OTHER);
    }

    LinkedHashMap<QPath, ColumnMetadata> getPaths() {
        return paths;
    }

    LinkedList<String> getForeignColumnNames() {
        return inverse ? columnNames : foreignColumns.stream()
                .map(QEntityColumnMetadata::getColumnName)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
