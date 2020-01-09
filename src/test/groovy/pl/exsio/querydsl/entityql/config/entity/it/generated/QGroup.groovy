package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.ForeignKey
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.QBase
import pl.exsio.querydsl.entityql.QColumnMetadataFactory
import pl.exsio.querydsl.entityql.QPathConfig
import pl.exsio.querydsl.entityql.QPathFactory

public final class QGroup extends QBase<pl.exsio.querydsl.entityql.config.entity.it.Group> {

  public static final QGroup INSTANCE = new QGroup();

  public final NumberPath<java.lang.Long> id;

  public final StringPath name;

  public final StringPath adminId;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin> admin;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.Group> _primaryKey;

  public QGroup() {
    this("GROUPS");
  }

  @SuppressWarnings(value = "unchecked")
  public QGroup(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.Group, variable, "", "GROUPS");

    id:
    {
      this.id =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "GROUP_ID", true, 1, -5))
                  .get());
      addMetadata(this.id, QColumnMetadataFactory.create("GROUP_ID", 1, -5, true));
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

    adminId:
    {
      this.adminId =
          ((StringPath)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.String.class,
                          java.lang.String.class,
                          "ADMIN_NAME",
                          false,
                          4,
                          12))
                  .get());
      addMetadata(this.adminId, QColumnMetadataFactory.create("ADMIN_NAME", 4, 12, false));
    }

    admin:
    {
      this.admin =
          ((ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin>)
              createForeignKey(this.adminId, "NAME"));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey =
          ((PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.Group>)
              createPrimaryKey(paths.toArray(new Path[0])));
    }
  }
}
