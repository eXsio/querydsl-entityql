package pl.exsio.querydsl.entityql.entity.metadata;

public class QEntityCompositeJoinColumnItemMetadata implements ReferenceColumnInfoMetadata {

    private final String columnName;

    private final String columnDefinition;

    private final String referencedColumnName;

    private final boolean nullable;


    public QEntityCompositeJoinColumnItemMetadata(String columnName, String columnDefinition,
                                                  String referencedColumnName, boolean nullable) {
        this.columnName = columnName;
        this.columnDefinition = columnDefinition;
        this.referencedColumnName = referencedColumnName;
        this.nullable = nullable;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getReferencedColumnName() {
        return referencedColumnName;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public String getColumnDefinition() {
        return columnDefinition;
    }
}
