package pl.exsio.querydsl.entityql.type;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.NamingStrategy;
import pl.exsio.querydsl.entityql.entity.scanner.runtime.UnderscoreToCamelStrategy;

import java.util.List;

public final class SimpleMapProjections {

    private SimpleMapProjections() { }

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
    public static SimpleMap map(Path<?>... exprs) {
        return map(UnderscoreToCamelStrategy.getInstance(), exprs);
    }

    public static SimpleMap map(List<Path<?>> exprs) {
        return map(UnderscoreToCamelStrategy.getInstance(), exprs);
    }

    public static SimpleMap map(Path<?>[]... exprs) {
        ImmutableList.Builder<Path<?>> builder = ImmutableList.builder();
        for (Path<?>[] expr: exprs) {
            builder.add(expr);
        }

        return new SimpleMap(UnderscoreToCamelStrategy.getInstance(), builder.build());
    }

    public static SimpleMap map(NamingStrategy namingStrategy, Path<?>... exprs) {
        return new SimpleMap(namingStrategy, exprs);
    }

    public static SimpleMap map(NamingStrategy namingStrategy, List<Path<?>> exprs) {
        return new SimpleMap(namingStrategy, exprs);
    }

}
