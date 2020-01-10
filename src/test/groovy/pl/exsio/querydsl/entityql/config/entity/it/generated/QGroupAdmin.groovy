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
    super(
        pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin.class, variable, "", "GROUP_ADMINS");

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

  public Q<pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin> dynamic(String variable) {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.GroupAdmin.class, variable);
  }
}
