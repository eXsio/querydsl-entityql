package pl.exsio.querydsl.entityql.type;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionBase;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Visitor;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.QRuntimeNamingStrategy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class QRuntimeMap extends FactoryExpressionBase<Map<String,?>> {

    private final ImmutableList<Path<?>> args;
    private final QRuntimeNamingStrategy namingStrategy;

    public QRuntimeMap(QRuntimeNamingStrategy namingStrategy, Path<?>... args) {
        super((Class) Map.class);
        this.namingStrategy = namingStrategy;
        this.args = ImmutableList.copyOf(args);
    }

    public QRuntimeMap(QRuntimeNamingStrategy namingStrategy, List<Path<?>> args) {
        super((Class) Map.class);
        this.namingStrategy = namingStrategy;
        this.args = ImmutableList.copyOf(args);
    }

    @Nullable
    @Override
    public <R, C> R accept(Visitor<R, C> v, @Nullable C context) {
        return v.visit(this, context);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof FactoryExpression) {
            FactoryExpression<?> c = (FactoryExpression<?>) obj;
            return args.equals(c.getArgs()) && getType().equals(c.getType());
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public Map<String, ?> newInstance(Object... args) {
        Map<String, Object> map = Maps.newHashMap();
        for (int i = 0; i < args.length; i++) {
            Path<?> key = this.args.get(i);
            String columnName = key.getMetadata().getName();
            String fieldName = namingStrategy.getFieldName(columnName);
            map.put(fieldName, args[i]);
        }

        return map;
    }

}
