package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.QBase
import pl.exsio.querydsl.entityql.QColumnMetadataFactory
import pl.exsio.querydsl.entityql.QPathConfig
import pl.exsio.querydsl.entityql.QPathFactory

public final class QGroupAdmin
    extends QBase<pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin> {

  public static final QGroupAdmin INSTANCE = new QGroupAdmin();

  public final NumberPath<java.lang.Long> id;

  public final StringPath name;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin> _primaryKey;

  public QGroupAdmin() {
    this("GROUP_ADMINS");
  }

  @SuppressWarnings(value = "unchecked")
  public QGroupAdmin(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin, variable, "", "GROUP_ADMINS");

    id:
    {
      this.id =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "GA_ID", true, 1, -5))
                  .get());
      addMetadata(this.id, QColumnMetadataFactory.create("GA_ID", 1, -5, true));
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

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey =
          ((PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin>)
              createPrimaryKey(paths.toArray(new Path[0])));
    }
  }
}
