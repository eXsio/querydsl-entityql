package pl.exsio.querydsl.entityql.config.entity.generated;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.PrimaryKey;
import pl.exsio.querydsl.entityql.*;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;

public final class QJCompositeFk
    extends QBase<pl.exsio.querydsl.entityql.config.entity.JCompositeFk> {

  public static final QJCompositeFk INSTANCE = new QJCompositeFk();

  public final NumberPath<java.lang.Long> id;

  public final StringPath desc;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.JCompositePk> compositePk;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.JSingularPk> singularPk;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.JCompositeFk> _primaryKey;

  public QJCompositeFk() {
    this("JCOMPOSITE_FK");
  }

  @SuppressWarnings(value = "unchecked")
  public QJCompositeFk(String variable) {
    super(
        pl.exsio.querydsl.entityql.config.entity.JCompositeFk.class, variable, "", "JCOMPOSITE_FK");

    id:
    {
      this.id =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "ID", true, 1, -5))
                  .get());
      addMetadata(this.id, QColumnMetadataFactory.create("ID", 1, -5, true));
    }

    desc:
    {
      this.desc =
          ((StringPath)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.String.class, java.lang.String.class, "DESC", true, 4, 12))
                  .get());
      addMetadata(this.desc, QColumnMetadataFactory.create("DESC", 4, 12, true));
    }

    compositePk:
    {
      List paths = new ArrayList();
      List<String> foreignColumnNames = new ArrayList<>();

      Path<?> compositePk0 =
          QPathFactory.create(
                  this,
                  new QPathConfig(
                      java.lang.Long.class, java.lang.Long.class, "CPK_ID_1", false, 2, -5))
              .get();
      addMetadata(compositePk0, QColumnMetadataFactory.create("CPK_ID_1", 2, -5, false));
      paths.add(compositePk0);

      Path<?> compositePk1 =
          QPathFactory.create(
                  this,
                  new QPathConfig(
                      java.lang.String.class, java.lang.String.class, "CPK_ID_2", false, 2, 12))
              .get();
      addMetadata(compositePk1, QColumnMetadataFactory.create("CPK_ID_2", 2, 12, false));
      paths.add(compositePk1);

      foreignColumnNames.add("ID_1");

      foreignColumnNames.add("ID_2");

      this.compositePk =
          this.<pl.exsio.querydsl.entityql.config.entity.JCompositePk>createForeignKey(
              paths, foreignColumnNames);
    }

    singularPk:
    {
      List paths = new ArrayList();
      List<String> foreignColumnNames = new ArrayList<>();

      Path<?> singularPk0 =
          QPathFactory.create(
                  this,
                  new QPathConfig(
                      java.lang.Long.class, java.lang.Long.class, "SPK_ID_1", false, 3, -5))
              .get();
      addMetadata(singularPk0, QColumnMetadataFactory.create("SPK_ID_1", 3, -5, false));
      paths.add(singularPk0);

      Path<?> singularPk1 =
          QPathFactory.create(
                  this,
                  new QPathConfig(
                      java.lang.String.class, java.lang.String.class, "SPK_ID_2", false, 3, 12))
              .get();
      addMetadata(singularPk1, QColumnMetadataFactory.create("SPK_ID_2", 3, 12, false));
      paths.add(singularPk1);

      foreignColumnNames.add("ID_1");

      foreignColumnNames.add("ID_2");

      this.singularPk =
          this.<pl.exsio.querydsl.entityql.config.entity.JSingularPk>createForeignKey(
              paths, foreignColumnNames);
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey = createPrimaryKey(paths.<Path>toArray(new Path[0]));
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

  public Q<pl.exsio.querydsl.entityql.config.entity.JCompositeFk> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.JCompositeFk.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.JCompositeFk> dynamic(String variable) {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.JCompositeFk.class, variable);
  }
}
