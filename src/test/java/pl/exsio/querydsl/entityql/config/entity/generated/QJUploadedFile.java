package pl.exsio.querydsl.entityql.config.entity.generated;

import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.sql.PrimaryKey;
import pl.exsio.querydsl.entityql.*;
import pl.exsio.querydsl.entityql.ex.InvalidArgumentException;
import pl.exsio.querydsl.entityql.path.QUuidPath;

import java.util.ArrayList;
import java.util.List;

public final class QJUploadedFile
    extends QBase<pl.exsio.querydsl.entityql.config.entity.JUploadedFile> {

  public static final QJUploadedFile INSTANCE = new QJUploadedFile();

  public final QUuidPath id;

  public final ArrayPath<byte[], java.lang.Byte> data;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.JUploadedFile> _primaryKey;

  public QJUploadedFile() {
    this("JUPLOADED_FILES");
  }

  @SuppressWarnings(value = "unchecked")
  public QJUploadedFile(String variable) {
    super(
        pl.exsio.querydsl.entityql.config.entity.JUploadedFile.class,
        variable,
        "",
        "JUPLOADED_FILES");

    id:
    {
      this.id =
          ((QUuidPath)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          java.util.UUID.class, java.util.UUID.class, "FILE_ID", false, 1, 12))
                  .get());
      addMetadata(this.id, QColumnMetadataFactory.create("FILE_ID", 1, 12, false));
    }

    data:
    {
      this.data =
          ((ArrayPath<byte[], java.lang.Byte>)
              QPathFactory.create(
                      this,
                      new QPathConfig(
                          byte[].class, java.lang.reflect.Array.class, "DATA", false, 2, 2003))
                  .get());
      addMetadata(this.data, QColumnMetadataFactory.create("DATA", 2, 2003, false));
    }

    _primaryKey:
    {
      List<Path> paths = new ArrayList();

      paths.add(this.id);

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

  public Q<pl.exsio.querydsl.entityql.config.entity.JUploadedFile> dynamic() {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.JUploadedFile.class);
  }

  public Q<pl.exsio.querydsl.entityql.config.entity.JUploadedFile> dynamic(String variable) {
    return EntityQL.qEntity(pl.exsio.querydsl.entityql.config.entity.JUploadedFile.class, variable);
  }
}
