package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.dml.StoreClause
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.*
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException

@groovy.transform.CompileStatic
public final class QBook extends QBase<pl.exsio.querydsl.entityql.config.entity.it.Book> {

  public static final QBook INSTANCE = new QBook();

  public final NumberPath<java.lang.Long> id;

  public final StringPath name;

  public final StringPath desc;

  public final NumberPath<java.math.BigDecimal> price;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.Book> _primaryKey;

  public QBook() {
    this("BOOKS");
  }

  @SuppressWarnings(value = "unchecked")
  public QBook(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.Book.class, variable, "", "BOOKS");

    id:
    {
      this.id =
          QPathFactory.<NumberPath<java.lang.Long>>create(
              this,
              new QPathConfig(java.lang.Long.class, java.lang.Long.class, "BOOK_ID", true, 1, -5));

      addMetadata(this.id, QColumnMetadataFactory.create("BOOK_ID", 1, -5, true));
    }

    name:
    {
      this.name =
          QPathFactory.<StringPath>create(
              this,
              new QPathConfig(java.lang.String.class, java.lang.String.class, "NAME", true, 2, 12));

      addMetadata(this.name, QColumnMetadataFactory.create("NAME", 2, 12, true));
    }

    desc:
    {
      this.desc =
          QPathFactory.<StringPath>create(
              this,
              new QPathConfig(
                  java.lang.String.class, java.lang.String.class, "DESC", true, 3, 2005));

      addMetadata(this.desc, QColumnMetadataFactory.create("DESC", 3, 2005, true));
    }

    price:
    {
      this.price =
          QPathFactory.<NumberPath<java.math.BigDecimal>>create(
              this,
              new QPathConfig(
                  java.math.BigDecimal.class, java.math.BigDecimal.class, "PRICE", true, 4, 3));

      addMetadata(this.price, QColumnMetadataFactory.create("PRICE", 4, 3, true));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey =
          this.<pl.exsio.querydsl.entityql.config.entity.it.Book>createPrimaryKey(
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

  public Q<pl.exsio.querydsl.entityql.config.entity.it.Book> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.Book.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.it.Book> dynamic(String variable) {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.Book.class, variable);
  }
}
