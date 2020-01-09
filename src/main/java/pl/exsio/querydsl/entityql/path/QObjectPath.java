package pl.exsio.querydsl.entityql.path;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.SimplePath;

import javax.annotation.Nullable;
import java.lang.reflect.AnnotatedElement;

public class QObjectPath<T> extends SimpleExpression<T> implements Path<T> {

    private final SimplePath<T> delegatePath;

    public QObjectPath(SimplePath<T> delegatePath) {
        super(delegatePath);
        this.delegatePath = delegatePath;
    }

    @Override
    public PathMetadata getMetadata() {
        return delegatePath.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return delegatePath.getRoot();
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return delegatePath.getAnnotatedElement();
    }

    @Nullable
    @Override
    public <R, C> R accept(Visitor<R, C> visitor, @Nullable C c) {
        return visitor.visit(delegatePath, c);
    }
}
