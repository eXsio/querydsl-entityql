package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.sql.ForeignKey
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.QBase
import pl.exsio.querydsl.entityql.QColumnMetadataFactory
import pl.exsio.querydsl.entityql.QPathConfig
import pl.exsio.querydsl.entityql.QPathFactory

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
    super(pl.exsio.querydsl.entityql.config.entity.it.Order, variable, "", "ORDERS");

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
          ((ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.User>)
              createForeignKey(this.userId, "USER_ID"));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey =
          ((PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.Order>)
              createPrimaryKey(paths.toArray(new Path[0])));
    }
  }
}
