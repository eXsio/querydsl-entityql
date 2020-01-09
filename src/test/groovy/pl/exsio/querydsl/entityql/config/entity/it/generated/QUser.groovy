package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.EnumPath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.QBase
import pl.exsio.querydsl.entityql.QColumnMetadataFactory
import pl.exsio.querydsl.entityql.QPathConfig
import pl.exsio.querydsl.entityql.QPathFactory
import pl.exsio.querydsl.entityql.path.ObjectPath

public final class QUser extends QBase<pl.exsio.querydsl.entityql.config.entity.it.User> {

  public static final QUser INSTANCE = new QUser();

  public final NumberPath<java.lang.Long> id;

  public final StringPath name;

  public final EnumPath<pl.exsio.querydsl.entityql.config.entity.it.User.Type> type;

  public final NumberPath<java.lang.Long> typeOrd;

  public final NumberPath<java.lang.Long> typeDef;

  public final ObjectPath<java.lang.Object> createdBy;

  public final ObjectPath<java.util.Date> createdAt;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.User> _primaryKey;

  public QUser() {
    this("USERS");
  }

  @SuppressWarnings(value = "unchecked")
  public QUser(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.User, variable, "", "USERS");

    id:
    {
      this.id =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Long.class, java.lang.Long.class, "USER_ID", true, 1, -5))
                  .get());
      addMetadata(this.id, QColumnMetadataFactory.create("USER_ID", 1, -5, true));
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

    type:
    {
      this.type =
          ((EnumPath<pl.exsio.querydsl.entityql.config.entity.it.User.Type>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          pl.exsio.querydsl.entityql.config.entity.it.User$Type.class,
                          java.lang.Enum.class,
                          "USER_TYPE",
                          false,
                          4,
                          12))
                  .get());
      addMetadata(this.type, QColumnMetadataFactory.create("USER_TYPE", 4, 12, false));
    }

    typeOrd:
    {
      this.typeOrd =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          pl.exsio.querydsl.entityql.config.entity.it.User$Type.class,
                          java.lang.Long.class,
                          "USER_TYPE_ORD",
                          false,
                          5,
                          -5))
                  .get());
      addMetadata(this.typeOrd, QColumnMetadataFactory.create("USER_TYPE_ORD", 5, -5, false));
    }

    typeDef:
    {
      this.typeDef =
          ((NumberPath<java.lang.Long>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          pl.exsio.querydsl.entityql.config.entity.it.User$Type.class,
                          java.lang.Long.class,
                          "USER_TYPE_DEF",
                          false,
                          6,
                          -5))
                  .get());
      addMetadata(this.typeDef, QColumnMetadataFactory.create("USER_TYPE_DEF", 6, -5, false));
    }

    createdBy:
    {
      this.createdBy =
          ((ObjectPath<java.lang.Object>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.lang.Object.class,
                          java.lang.Object.class,
                          "CREATED_BY",
                          true,
                          7,
                          1111))
                  .get());
      addMetadata(this.createdBy, QColumnMetadataFactory.create("CREATED_BY", 7, 1111, true));
    }

    createdAt:
    {
      this.createdAt =
          ((ObjectPath<java.util.Date>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.util.Date.class,
                          java.lang.Object.class,
                          "CREATED_AT",
                          true,
                          8,
                          1111))
                  .get());
      addMetadata(this.createdAt, QColumnMetadataFactory.create("CREATED_AT", 8, 1111, true));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey =
          ((PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.User>)
              createPrimaryKey(paths.toArray(new Path[0])));
    }
  }
}
