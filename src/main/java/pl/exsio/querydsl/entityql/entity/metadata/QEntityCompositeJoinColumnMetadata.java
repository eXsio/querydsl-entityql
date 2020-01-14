package pl.exsio.querydsl.entityql.entity.metadata;

import java.util.LinkedList;
import java.util.List;

public class QEntityCompositeJoinColumnMetadata {

    private final Class<?> fieldType;

    private final String fieldName;

    private final List<QEntityCompositeJoinColumnItemMetadata> items = new LinkedList<>();

    private final int idx;

    public QEntityCompositeJoinColumnMetadata(Class<?> fieldType, String fieldName, int idx) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.idx = idx;
    }


    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public int getIdx() {
        return idx;
    }

    public List<QEntityCompositeJoinColumnItemMetadata> getItems() {
        return items;
    }

    public void addItem(QEntityCompositeJoinColumnItemMetadata item) {
        items.add(item);
    }
}
