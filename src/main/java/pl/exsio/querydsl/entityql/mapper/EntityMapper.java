package pl.exsio.querydsl.entityql.mapper;

import com.querydsl.core.QueryException;
import com.querydsl.core.types.Path;
import com.querydsl.core.util.ReflectionUtils;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.dml.Mapper;
import com.querydsl.sql.types.Null;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * For <code>SQLInsertClause.populate()</code> <br/>
 * Creates the mapping via <code>javax.persistence.Column</code> annotated fields in the object.<br/>
 * Field names don't have to match those in the RelationalPath.
 *
 * @author jojoldu
 */
public class EntityMapper implements Mapper<Object> {
    public static final EntityMapper DEFAULT = new EntityMapper(false);

    public static final EntityMapper WITH_NULL_BINDINGS = new EntityMapper(true);

    private final boolean withNullBindings;

    public EntityMapper(boolean withNullBindings) {
        this.withNullBindings = withNullBindings;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> path, Object object) {
        try {
            Map<String, Path<?>> columnToPath = new HashMap<>();
            for (Path<?> column : path.getColumns()) {
                columnToPath.put(ColumnMetadata.getName(column), column);
            }
            Map<Path<?>, Object> values = new HashMap<>();
            for (Field field : ReflectionUtils.getFields(object.getClass())) {
                Column ann = field.getAnnotation(Column.class);
                if (ann != null) {
                    field.setAccessible(true);
                    Object propertyValue = field.get(object);
                    if (propertyValue != null) {
                        if (columnToPath.containsKey(ann.name())) {
                            values.put(columnToPath.get(ann.name()), propertyValue);
                        }
                    } else if (withNullBindings) {
                        values.put(columnToPath.get(ann.name()), Null.DEFAULT);
                    }
                }
            }
            return values;
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        }

    }
}
