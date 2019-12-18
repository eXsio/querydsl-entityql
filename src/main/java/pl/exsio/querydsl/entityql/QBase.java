package pl.exsio.querydsl.entityql;

import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.RelationalPathBase;

class QBase<E> extends RelationalPathBase<E> {

    QBase(Class<? extends E> type, String variable, String schema, String table) {
        super(type, variable, schema, table);
    }

    @Override
    protected <A, E1> ArrayPath<A, E1> createArray(String property, Class<? super A> type) {
        return super.createArray(property, type);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <A extends Number & Comparable<?>> NumberPath<A> createNumber(String property, Class<? super A> type) {
        return super.createNumber(property, (Class<A>)type);
    }

    @Override
    protected StringPath createString(String property) {
        return super.createString(property);
    }

    @Override
    protected <A extends Comparable> DatePath<A> createDate(String property, Class<? super A> type) {
        return super.createDate(property, type);
    }

    @Override
    protected <A extends Comparable> DateTimePath<A> createDateTime(String property, Class<? super A> type) {
        return super.createDateTime(property, type);
    }

    @Override
    protected <A> SimplePath<A> createSimple(String property, Class<? super A> type) {
        return super.createSimple(property, type);
    }

    @Override
    protected <A extends Comparable> TimePath<A> createTime(String property, Class<? super A> type) {
        return super.createTime(property, type);
    }
}
