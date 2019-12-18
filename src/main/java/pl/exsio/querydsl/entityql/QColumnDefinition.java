package pl.exsio.querydsl.entityql;

import javax.persistence.Column;
import java.lang.reflect.Field;

class QColumnDefinition {

    private final Field field;

    private final Column column;

    QColumnDefinition(Field field, Column column) {
        this.field = field;
        this.column = column;
    }

    Field getField() {
        return field;
    }

    public Column getColumn() {
        return column;
    }
}
