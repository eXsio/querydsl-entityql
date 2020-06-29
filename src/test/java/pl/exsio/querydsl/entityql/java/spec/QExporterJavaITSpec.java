package pl.exsio.querydsl.entityql.java.spec;

import org.junit.Ignore;
import org.junit.Test;
import pl.exsio.querydsl.entityql.QExporter;
import pl.exsio.querydsl.entityql.java.jpa.entity.*;

import java.nio.file.Paths;

import static pl.exsio.querydsl.entityql.EntityQL.qEntity;

public class QExporterJavaITSpec {

    String pkgName = "pl.exsio.querydsl.entityql.jpa.entity.generated";

    String fileNamePattern = "Q%s.java";

    String destinationPath = Paths.get("src/test/java").toAbsolutePath().toString();

    QExporter exporter = new QExporter();

    @Test
   // @Ignore
    public void shouldExportIntegrationQClassesToStaticMetaModels() throws Exception {
        exporter.export(qEntity(JBook.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JCompositePk.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JCompositeFk.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JGroup.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JGroupAdmin.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JOrder.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JOrderItem.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JSingularPk.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JUploadedFile.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JUser.class), fileNamePattern, pkgName, destinationPath);
        exporter.export(qEntity(JUserGroup.class), fileNamePattern, pkgName, destinationPath);
    }
}
