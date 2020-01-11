package pl.exsio.querydsl.entityql;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.Path;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;

public class QStaticModel<E> extends QBase<E> {

    public QStaticModel(Class<? extends E> type, String variable, String schema, String table) {
        super(type, variable, schema, table);
    }

    @SuppressWarnings(value = "unchecked")
    public <C extends StoreClause<C>> StoreClause<C> set(StoreClause<C> clause, Object... params) {
        if (params.length % 2 != 0) {
            throw new InvalidArgumentException("Odd number of parameters");
        }
        for (int i = 0; i < params.length - 1; i += 2) {
            Object key = params[i];
            Object value = params[i + 1];
            if (!(key instanceof Path)) {
                throw new InvalidArgumentException("Param key has to be Path");
            }
            clause.set((Path<Object>) key, value);
        }
        return clause;
    }

    @SuppressWarnings(value = "unchecked")
    public Q<E> dynamic() {
        Class<E> type = (Class<E>) getType();
        return EntityQL.qEntity(type);
    }

    @SuppressWarnings(value = "unchecked")
    public Q<E> dynamic(String variable) {
        Class<E> type = (Class<E>) getType();
        return EntityQL.qEntity(type, variable);
    }
}
