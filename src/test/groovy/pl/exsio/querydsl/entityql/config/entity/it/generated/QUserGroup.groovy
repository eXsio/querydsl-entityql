package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.sql.ForeignKey
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.QBase
import pl.exsio.querydsl.entityql.QColumnMetadataFactory
import pl.exsio.querydsl.entityql.QPathConfig
import pl.exsio.querydsl.entityql.QPathFactory

public final class QUserGroup extends QBase<pl.exsio.querydsl.entityql.config.entity.it.UserGroup> {

  public static final QUserGroup INSTANCE = new QUserGroup();

  public final NumberPath<java.lang.Long> groupId;

  public final NumberPath<java.lang.Long> userId;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.Group> group;

  public final ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.User> user;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.UserGroup> _primaryKey;

  public QUserGroup() {
    this("USERS_GROUPS");
  }

  @SuppressWarnings(value = "unchecked")
  public QUserGroup(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.UserGroup, variable, "", "USERS_GROUPS");

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
          ((ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.Group>)
              createForeignKey(this.groupId, "GROUP_ID"));
    }

    user:
    {
      this.user =
          ((ForeignKey<pl.exsio.querydsl.entityql.config.entity.it.User>)
              createForeignKey(this.userId, "USER_ID"));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.groupId);

      paths.add(this.userId);

      this._primaryKey =
          ((PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.UserGroup>)
              createPrimaryKey(paths.toArray(new Path[0])));
    }
  }
}
