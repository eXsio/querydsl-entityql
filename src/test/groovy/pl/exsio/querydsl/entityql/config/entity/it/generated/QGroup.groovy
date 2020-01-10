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
    super(pl.exsio.querydsl.entityql.config.entity.it.Group.class, variable, "", "GROUPS");

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
          this.<pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin>createForeignKey(
              this.adminId, "NAME");
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

  public Q<pl.exsio.querydsl.entityql.config.entity.it.Group> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.Group.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.it.Group> dynamic(String variable) {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.Group.class, variable);
  }
}
