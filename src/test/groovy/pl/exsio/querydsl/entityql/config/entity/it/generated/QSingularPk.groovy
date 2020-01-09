package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.QBase
import pl.exsio.querydsl.entityql.QColumnMetadataFactory
import pl.exsio.querydsl.entityql.QPathConfig
import pl.exsio.querydsl.entityql.QPathFactory

public final class QSingularPk
    extends QBase<pl.exsio.querydsl.entityql.config.entity.it.SingularPk> {

  public static final QSingularPk INSTANCE = new QSingularPk();

  public final NumberPath<java.lang.Long> id1;

  public final StringPath id2;

  public final StringPath desc;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.SingularPk> _primaryKey;

  public QSingularPk() {
    this("SINGULAR_PK");
  }

  @SuppressWarnings(value = "unchecked")
  public QSingularPk(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.SingularPk, variable, "", "SINGULAR_PK");

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

      this._primaryKey =
          ((PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.SingularPk>)
              createPrimaryKey(paths.toArray(new Path[0])));
    }
  }
}
