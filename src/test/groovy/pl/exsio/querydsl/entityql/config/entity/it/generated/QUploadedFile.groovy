package pl.exsio.querydsl.entityql.config.entity.it.generated

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.ArrayPath
import com.querydsl.sql.PrimaryKey
import pl.exsio.querydsl.entityql.QBase
import pl.exsio.querydsl.entityql.QColumnMetadataFactory
import pl.exsio.querydsl.entityql.QPathConfig
import pl.exsio.querydsl.entityql.QPathFactory
import pl.exsio.querydsl.entityql.path.UuidPath

public final class QUploadedFile
    extends QBase<pl.exsio.querydsl.entityql.config.entity.it.UploadedFile> {

  public static final QUploadedFile INSTANCE = new QUploadedFile();

  public final UuidPath id;

  public final ArrayPath<byte[], java.lang.Byte> data;

  public final PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.UploadedFile> _primaryKey;

  public QUploadedFile() {
    this("UPLOADED_FILES");
  }

  @SuppressWarnings(value = "unchecked")
  public QUploadedFile(String variable) {
    super(pl.exsio.querydsl.entityql.config.entity.it.UploadedFile, variable, "", "UPLOADED_FILES");

    id:
    {
      this.id =
          ((UuidPath)
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

      this._primaryKey =
          ((PrimaryKey<pl.exsio.querydsl.entityql.config.entity.it.UploadedFile>)
              createPrimaryKey(paths.toArray(new Path[0])));
    }
  }
}
