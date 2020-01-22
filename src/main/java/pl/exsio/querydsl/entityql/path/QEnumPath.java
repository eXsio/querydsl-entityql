package pl.exsio.querydsl.entityql.path;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.EnumPath;

/**
 * Custom Enum path for handling Java Enums in QueryDSL
 */
public class QEnumPath<T extends Enum<T>> extends EnumPath<T> {

    public QEnumPath(Class<? extends T> type, Path<?> parent, String property) {
        super(type, parent, property);
    }
}
