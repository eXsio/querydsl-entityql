package pl.exsio.querydsl.entityql.config.entity.generated;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.PrimaryKey;
import pl.exsio.querydsl.entityql.*;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;

public final class QJUserGroup extends QBase<pl.exsio.querydsl.entityql.config.entity.JUserGroup> {

  public static final QJUserGroup INSTANCE = new QJUserGroup();

  public final NumberPath<java.lang.Long> groupId;

  public final NumberPath<java.lang.Long> userId;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.JGroup> group;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.JUser> user;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.JUserGroup> _primaryKey;

  public QJUserGroup() {
    this("JUSERS_GROUPS");
  }

  @SuppressWarnings(value = "unchecked")
  public QJUserGroup(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.JUserGroup.class, variable, "", "JUSERS_GROUPS");

    groupId:
    {
      this.groupId =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "GROUP_ID", true, 3, -5))
                  .get());
      addMetadata(this.groupId, QColumnMetadataFactory.create("GROUP_ID", 3, -5, true));
    }

    userId:
    {
      this.userId =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "USER_ID", true, 4, -5))
                  .get());
      addMetadata(this.userId, QColumnMetadataFactory.create("USER_ID", 4, -5, true));
    }

    group:
    {
      this.group =
          this.<pl.exsio.querydsl.entityql.config.entity.JGroup>createForeignKey(
              this.groupId, "GROUP_ID");
    }

    user:
    {
      this.user =
          this.<pl.exsio.querydsl.entityql.config.entity.JUser>createForeignKey(
              this.userId, "USER_ID");
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.groupId);

      paths.add(this.userId);

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

  public Q<pl.exsio.querydsl.entityql.config.entity.JUserGroup> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.JUserGroup.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.JUserGroup> dynamic(String variable) {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.JUserGroup.class, variable);
  }
}
