package pl.exsio.querydsl.entityql;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.ForeignKey;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class QForeignKey {

    private final ForeignKey<?> foreignKey;

    private final Class<?> parametrizedType;

    private final LinkedHashMap<QPath, ColumnMetadata> paths;

    private final LinkedList<String> foreignColumnNames;

    QForeignKey(ForeignKey<?> foreignKey, Class<?> parametrizedType,
                LinkedHashMap<QPath, ColumnMetadata> paths,
                LinkedList<String> foreignColumnNames) {
        this.foreignKey = foreignKey;
        this.parametrizedType = parametrizedType;
        this.paths = paths;
        this.foreignColumnNames = foreignColumnNames;
    }

    public ForeignKey<?> get() {
        return foreignKey;
    }

    public Class<?> getParametrizedType() {
        return parametrizedType;
    }

    public LinkedHashMap<QPath, ColumnMetadata> getPaths() {
        return paths;
    }

    public LinkedList<String> getForeignColumnNames() {
        return foreignColumnNames;
    }
}
