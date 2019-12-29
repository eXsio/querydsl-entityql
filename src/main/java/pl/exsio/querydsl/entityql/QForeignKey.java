package pl.exsio.querydsl.entityql;

import com.querydsl.sql.ForeignKey;

import java.lang.reflect.Field;

public class QForeignKey {

    private final ForeignKey<?> foreignKey;

    private final Class<?> parametrizedType;

    QForeignKey(ForeignKey<?> foreignKey, Field field) {
        this.foreignKey = foreignKey;
        this.parametrizedType = field.getType();
    }

    public ForeignKey<?> get() {
        return foreignKey;
    }

    public Class<?> getParametrizedType() {
        return parametrizedType;
    }
}
