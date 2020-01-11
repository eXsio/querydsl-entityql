package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.dml.StoreClause
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.sql.ForeignKey
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.*
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException

@groovy.transform.CompileStatic
public final class QOrderItem extends QBase<pl.exsio.querydsl.entityql.config.entity.it.OrderItem> {

  public static final QOrderItem INSTANCE = new QOrderItem();

  public final NumberPath<java.lang.Long> id;

  public final NumberPath<java.lang.Long> quantity;

  public final NumberPath<java.lang.Long> bookId;

  public final NumberPath<java.lang.Long> orderId;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.Book> book;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.Order> order;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.OrderItem> _primaryKey;

  public QOrderItem() {
    this("ORDER_ITEMS");
  }

  @SuppressWarnings(value = "unchecked")
  public QOrderItem(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.OrderItem.class, variable, "", "ORDER_ITEMS");

    id:
    {
      this.id =
          QPathFactory.<NumberPath<java.lang.Long>>create(
              this,
              new QPathConfig(
                  java.lang.Long.class, java.lang.Long.class, "ORDER_ITEM_ID", true, 1, -5));

      addMetadata(this.id, QColumnMetadataFactory.create("ORDER_ITEM_ID", 1, -5, true));
    }

    quantity:
    {
      this.quantity =
          QPathFactory.<NumberPath<java.lang.Long>>create(
              this,
              new QPathConfig(java.lang.Long.class, java.lang.Long.class, "QTY", false, 4, -5));

      addMetadata(this.quantity, QColumnMetadataFactory.create("QTY", 4, -5, false));
    }

    bookId:
    {
      this.bookId =
          QPathFactory.<NumberPath<java.lang.Long>>create(
              this,
              new QPathConfig(java.lang.Long.class, java.lang.Long.class, "BOOK_ID", false, 2, -5));

      addMetadata(this.bookId, QColumnMetadataFactory.create("BOOK_ID", 2, -5, false));
    }

    orderId:
    {
      this.orderId =
          QPathFactory.<NumberPath<java.lang.Long>>create(
              this,
              new QPathConfig(
                  java.lang.Long.class, java.lang.Long.class, "ORDER_ID", false, 3, -5));

      addMetadata(this.orderId, QColumnMetadataFactory.create("ORDER_ID", 3, -5, false));
    }

    book:
    {
      this.book =
          this.<pl.exsio.querydsl.entityql.config.entity.it.Book>createForeignKey(
              this.bookId, "BOOK_ID");
    }

    order:
    {
      this.order =
          this.<pl.exsio.querydsl.entityql.config.entity.it.Order>createForeignKey(
              this.orderId, "ORDER_ID");
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey =
          this.<pl.exsio.querydsl.entityql.config.entity.it.OrderItem>createPrimaryKey(
              paths.<Path>toArray(new Path[0]));
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

  public Q<pl.exsio.querydsl.entityql.config.entity.it.OrderItem> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.OrderItem.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.it.OrderItem> dynamic(String variable) {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.OrderItem.class, variable);
  }
}
