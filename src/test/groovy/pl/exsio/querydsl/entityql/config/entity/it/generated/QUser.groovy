package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.dml.StoreClause
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.*
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException
import pl.exsio.querydsl.entityql.path.QEnumPath
import pl.exsio.querydsl.entityql.path.QObjectPath

@groovy.transform.CompileStatic
public final class QUser extends QBase<pl.exsio.querydsl.entityql.config.entity.it.User> {

  public static final QUser INSTANCE = new QUser();

  public final NumberPath<java.lang.Long> id;

  public final StringPath name;

  public final QEnumPath<pl.exsio.querydsl.entityql.config.entity.it.User.Type> typeStr;

  public final NumberPath<java.lang.Long> typeOrd;

  public final NumberPath<java.lang.Long> typeDef;

  public final QObjectPath<java.lang.Object> createdBy;

  public final QObjectPath<java.util.Date> createdAt;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.User> _primaryKey;

  public QUser() {
    this("USERS");
  }

  @SuppressWarnings(value = "unchecked")
  public QUser(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.User.class, variable, "", "USERS");

    id:
    {
      this.id =
          QPathFactory.<NumberPath<java.lang.Long>>create(
              this,
              new QPathConfig(java.lang.Long.class, java.lang.Long.class, "USER_ID", true, 1, -5));

      addMetadata(this.id, QColumnMetadataFactory.create("USER_ID", 1, -5, true));
    }

    name:
    {
      this.name =
          QPathFactory.<StringPath>create(
              this,
              new QPathConfig(java.lang.String.class, java.lang.String.class, "NAME", true, 2, 12));

      addMetadata(this.name, QColumnMetadataFactory.create("NAME", 2, 12, true));
    }

    typeStr:
    {
      this.typeStr =
          QPathFactory.<QEnumPath<pl.exsio.querydsl.entityql.config.entity.it.User.Type>>create(
              this,
              new QPathConfig(
                  pl.exsio.querydsl.entityql.config.entity.it.User.Type.class,
                  java.lang.Enum.class,
                  "USER_TYPE",
                  false,
                  4,
                  12));
      addMetadata(this.typeStr, QColumnMetadataFactory.create("USER_TYPE", 4, 12, false));
    }

    typeOrd:
    {
      this.typeOrd =
          QPathFactory.<NumberPath<java.lang.Long>>create(
              this,
              new QPathConfig(
                  pl.exsio.querydsl.entityql.config.entity.it.User.Type.class,
                  java.lang.Long.class,
                  "USER_TYPE_ORD",
                  false,
                  5,
                  -5));
      addMetadata(this.typeOrd, QColumnMetadataFactory.create("USER_TYPE_ORD", 5, -5, false));
    }

    typeDef:
    {
      this.typeDef =
          QPathFactory.<NumberPath<java.lang.Long>>create(
              this,
              new QPathConfig(
                  pl.exsio.querydsl.entityql.config.entity.it.User.Type.class,
                  java.lang.Long.class,
                  "USER_TYPE_DEF",
                  false,
                  6,
                  -5));
      addMetadata(this.typeDef, QColumnMetadataFactory.create("USER_TYPE_DEF", 6, -5, false));
    }

    createdBy:
    {
      this.createdBy =
          QPathFactory.<QObjectPath<java.lang.Object>>create(
              this,
              new QPathConfig(
                  java.lang.Object.class, java.lang.Object.class, "CREATED_BY", true, 7, 1111));

      addMetadata(this.createdBy, QColumnMetadataFactory.create("CREATED_BY", 7, 1111, true));
    }

    createdAt:
    {
      this.createdAt =
          QPathFactory.<QObjectPath<java.util.Date>>create(
              this,
              new QPathConfig(
                  java.util.Date.class, java.lang.Object.class, "CREATED_AT", true, 8, 1111));

      addMetadata(this.createdAt, QColumnMetadataFactory.create("CREATED_AT", 8, 1111, true));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey =
          this.<pl.exsio.querydsl.entityql.config.entity.it.User>createPrimaryKey(
              paths.<Path>toArray(new Path[0]));
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

  public Q<pl.exsio.querydsl.entityql.config.entity.it.User> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.User.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.it.User> dynamic(String variable) {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.User.class, variable);
  }
}
