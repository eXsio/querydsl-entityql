package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.QBase
import pl.exsio.querydsl.entityql.QColumnMetadataFactory
import pl.exsio.querydsl.entityql.QPathConfig
import pl.exsio.querydsl.entityql.QPathFactory

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
    super(pl.exsio.querydsl.entityql.config.entity.it.Book, variable, "", "BOOKS");

    id:
    {
      this.id =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "BOOK_ID", true, 1, -5))
                  .get());
      addMetadata(this.id, QColumnMetadataFactory.create("BOOK_ID", 1, -5, true));
    }

    name:
    {
      this.name =
          ((StringPath)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.String.class, java.lang.String.class, "NAME", true, 2, 12))
                  .get());
      addMetadata(this.name, QColumnMetadataFactory.create("NAME", 2, 12, true));
    }

    desc:
    {
      this.desc =
          ((StringPath)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.String.class, java.lang.String.class, "DESC", true, 3, 2005))
                  .get());
      addMetadata(this.desc, QColumnMetadataFactory.create("DESC", 3, 2005, true));
    }

    price:
    {
      this.price =
          ((NumberPath<java.math.BigDecimal>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.math.BigDecimal.class,
                          java.math.BigDecimal.class,
                          "PRICE",
                          true,
                          4,
                          3))
                  .get());
      addMetadata(this.price, QColumnMetadataFactory.create("PRICE", 4, 3, true));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey =
          ((PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.Book>)
              createPrimaryKey(paths.toArray(new Path[0])));
    }
  }
}
