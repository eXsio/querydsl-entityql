package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.dml.StoreClause
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.ArrayPath
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.*
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException
import pl.exsio.querydsl.entityql.path.QUuidPath

@groovy.transform.CompileStatic
public final class QUploadedFile
    extends QBase<pl.exsio.querydsl.entityql.config.entity.it.UploadedFile> {

  public static final QUploadedFile INSTANCE = new QUploadedFile();

  public final QUuidPath id;

  public final ArrayPath<byte[], java.lang.Byte> data;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.UploadedFile> _primaryKey;

  public QUploadedFile() {
    this("UPLOADED_FILES");
  }

  @SuppressWarnings(value = "unchecked")
  public QUploadedFile(String variable) {
    super(
        pl.exsio.querydsl.entityql.config.entity.it.UploadedFile.class,
        variable,
        "",
        "UPLOADED_FILES");

    id:
    {
      this.id =
          QPathFactory.<QUuidPath>create(
              this,
              new QPathConfig(java.util.UUID.class, java.util.UUID.class, "FILE_ID", false, 1, 12));

      addMetadata(this.id, QColumnMetadataFactory.create("FILE_ID", 1, 12, false));
    }

    data:
    {
      this.data =
          QPathFactory.<ArrayPath<byte[], java.lang.Byte>>create(
              this,
              new QPathConfig(byte[].class, java.lang.reflect.Array.class, "DATA", false, 2, 2003));

      addMetadata(this.data, QColumnMetadataFactory.create("DATA", 2, 2003, false));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

      this._primaryKey =
          this.<pl.exsio.querydsl.entityql.config.entity.it.UploadedFile>createPrimaryKey(
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

  public Q<pl.exsio.querydsl.entityql.config.entity.it.UploadedFile> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.it.UploadedFile.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.it.UploadedFile> dynamic(String variable) {
    return EntityQL.qEntity(
        pl.exsio.querydsl.entityql.config.entity.it.UploadedFile.class, variable);
  }
}
