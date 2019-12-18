package pl.exsio.querydsl.entityql;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;

import java.util.LinkedList;
import java.util.List;

public class EntityQL {

    public static <E> Q<E> qEntity(Class<E> entityClass) {
        return QFactory.get(entityClass).create(true);
    }

    public static <E> Q<E> qEntity(Class<E> entityClass, String variable) {
        return QFactory.get(entityClass).create(variable, true);
    }

    static <E> Q<E> qEntityWithoutMappings(Class<E> entityClass) {
        return QFactory.get(entityClass).create(false);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> ConstructorExpression<T> dto(Class<? extends T> dtoClass, List<Expression<?>>... expressionList) {
        List<Expression<?>> allExpressions = new LinkedList<>();
        for (List<Expression<?>> expressions : expressionList) {
            allExpressions.addAll(expressions);
        }
        return Projections.constructor(dtoClass, allExpressions.toArray(new Expression[0]));
    }
}
