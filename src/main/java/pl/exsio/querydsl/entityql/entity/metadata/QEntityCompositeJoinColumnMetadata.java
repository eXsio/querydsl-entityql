package pl.exsio.querydsl.entityql.entity.metadata;

import java.util.LinkedList;
import java.util.List;

/**
 * JoinColumn Metadata of Java Field - Composite DB Foreign Key mapping
 */
public class QEntityCompositeJoinColumnMetadata {

    /**
     * Class of the Java Field, in most cases read using Reflection
     */
    private final Class<?> fieldType;

    /**
     * Java field name. This name will be used in dynamic and static EntityQL Models
     * as a Column Identifier used in Java code
     */
    private final String fieldName;

    /**
     * DB Columns that make the Composite Foreign Key
     */
    private final List<QEntityCompositeJoinColumnItemMetadata> items = new LinkedList<>();

    /**
     * Index of the Field in Entity Class
     */
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

    @Override
    public String toString() {
        return "QEntityCompositeJoinColumnMetadata{" +
                "fieldType=" + fieldType +
                ", fieldName='" + fieldName + '\'' +
                ", items=" + items +
                ", idx=" + idx +
                '}';
    }
}
