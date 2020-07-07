package pl.exsio.querydsl.entityql.type;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.QRuntimeNamingStrategy;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.UnderscoreToCamelStrategyQRuntimeNamingStrategy;

import java.util.List;

public final class QRuntimeProjections {

    private QRuntimeProjections() { }

    /**
     * Create a simple Map typed projection for the given expressions, with string keys
     *
     * <p>Example</p>
     * <pre>{@code
     * Map<Path<?>, ?> map = query.select(
     *      SimpleMapProjections.map(user.firstName, user.lastName));
     * }</pre>
     *
     * Main difference with {@link com.querydsl.core.types.Projections#map(Expression[])} that is this method return simple hashmap with String keys.
     *
     * @param exprs arguments for the projection
     * @return factory expression
     */
    public static QRuntimeMap map(Path<?>... exprs) {
        return map(UnderscoreToCamelStrategyQRuntimeNamingStrategy.getInstance(), exprs);
    }

    public static QRuntimeMap map(List<Path<?>> exprs) {
        return map(UnderscoreToCamelStrategyQRuntimeNamingStrategy.getInstance(), exprs);
    }

    public static QRuntimeMap map(Path<?>[]... exprs) {
        ImmutableList.Builder<Path<?>> builder = ImmutableList.builder();
        for (Path<?>[] expr: exprs) {
            builder.add(expr);
        }

        return new QRuntimeMap(UnderscoreToCamelStrategyQRuntimeNamingStrategy.getInstance(), builder.build());
    }

    public static QRuntimeMap map(QRuntimeNamingStrategy namingStrategy, Path<?>... exprs) {
        return new QRuntimeMap(namingStrategy, exprs);
    }

    public static QRuntimeMap map(QRuntimeNamingStrategy namingStrategy, List<Path<?>> exprs) {
        return new QRuntimeMap(namingStrategy, exprs);
    }

}
