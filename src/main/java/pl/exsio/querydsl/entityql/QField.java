package pl.exsio.querydsl.entityql;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

abstract class QField {

    static Class<?> getType(Field field) {
        Class<?> type = field.getType();
        if (type.isArray()) {
            type = Array.class;
        } else if (type.isEnum()) {
            Enumerated enumerated = field.getDeclaredAnnotation(Enumerated.class);
            if (enumerated != null) {
                type = enumerated.value().equals(EnumType.ORDINAL) ? Long.class : Enum.class;
            } else {
                type = Long.class;
            }
        }
        return type;
    }

}
