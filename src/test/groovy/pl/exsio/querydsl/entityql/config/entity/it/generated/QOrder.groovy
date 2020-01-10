package pl.exsio.querydsl.entityql.config.entity.it.generated;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.*;
import java.util.ArrayList;
import java.util.List;
import pl.exsio.querydsl.entityql.*;
import pl.exsio.querydsl.entityql.ex.*;
import pl.exsio.querydsl.entityql.path.*;
import pl.exsio.querydsl.entityql.type.*;

public final class QOrder extends QBase<pl.exsio.querydsl.entityql.config.entity.it.Order> {

  public static final QOrder INSTANCE = new QOrder();

  public final NumberPath<java.lang.Long> id;

  public final NumberPath<java.lang.Long> userId;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.User> user;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.Order> _primaryKey;

  public QOrder() {
    this("ORDERS");
  }

  @SuppressWarnings(value = "unchecked")
  public QOrder(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.Order.class, variable, "", "ORDERS");

    id:
    {
      this.id =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "ORDER_ID", true, 1, -5))
                  .get());
      addMetadata(this.id, QColumnMetadataFactory.create("ORDER_ID", 1, -5, true));
    }

    userId:
    {
      this.userId =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "USER_ID", false, 2, -5))
                  .get());
      addMetadata(this.userId, QColumnMetadataFactory.create("USER_ID", 2, -5, false));
    }

    user:
    {
      this.user =
          this.<pl.exsio.querydsl.entityql.config.entity.it.User>createForeignKey(
              this.userId, "USER_ID");
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey = createPrimaryKey(paths.toArray(new Path[0]));
    }
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

  public Q<pl.exsio.querydsl.entityql.config.entity.it.Order> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.Order.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.it.Order> dynamic(String variable) {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.Order.class, variable);
  }
}
