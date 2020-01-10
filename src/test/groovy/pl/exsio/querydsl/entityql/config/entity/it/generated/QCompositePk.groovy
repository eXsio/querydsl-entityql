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

public final class QCompositePk
    extends QBase<pl.exsio.querydsl.entityql.config.entity.it.CompositePk> {

  public static final QCompositePk INSTANCE = new QCompositePk();

  public final NumberPath<java.lang.Long> id1;

  public final StringPath id2;

  public final StringPath desc;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.CompositePk> _primaryKey;

  public QCompositePk() {
    this("COMPOSITE_PK");
  }

  @SuppressWarnings(value = "unchecked")
  public QCompositePk(String variable) {
    super(
        pl.exsio.querydsl.entityql.config.entity.it.CompositePk.class,
        variable,
        "",
        "COMPOSITE_PK");

    id1:
    {
      this.id1 =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "ID_1", true, 1, -5))
                  .get());
      addMetadata(this.id1, QColumnMetadataFactory.create("ID_1", 1, -5, true));
    }

    id2:
    {
      this.id2 =
          ((StringPath)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.String.class, java.lang.String.class, "ID_2", true, 2, 12))
                  .get());
      addMetadata(this.id2, QColumnMetadataFactory.create("ID_2", 2, 12, true));
    }

    desc:
    {
      this.desc =
          ((StringPath)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.String.class, java.lang.String.class, "DESC", true, 3, 12))
                  .get());
      addMetadata(this.desc, QColumnMetadataFactory.create("DESC", 3, 12, true));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id1);

      paths.add(this.id2);

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

  public Q<pl.exsio.querydsl.entityql.config.entity.it.CompositePk> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.CompositePk.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.it.CompositePk> dynamic(String variable) {
    return EntityQL.qEntity(
        pl.exsio.querydsl.entityql.config.entity.it.CompositePk.class, variable);
  }
}
