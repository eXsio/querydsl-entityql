package pl.exsio.querydsl.entityql.path;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.LiteralExpression;

import java.lang.reflect.AnnotatedElement;

public class EnumPath<T extends Enum<T>> extends LiteralExpression<T> implements Path<T> {

    private static final long serialVersionUID = 7983490925756833429L;

    private final PathImpl<T> pathMixin;

    public EnumPath(Class<? extends T> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    private EnumPath(Class<? extends T> type, PathMetadata metadata) {
        super(ExpressionUtils.path(type, metadata));
        this.pathMixin = (PathImpl<T>) mixin;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(pathMixin, context);
    }

    @Override
    public PathMetadata getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return pathMixin.getAnnotatedElement();
    }
}
