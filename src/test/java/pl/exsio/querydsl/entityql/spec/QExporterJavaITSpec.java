package pl.exsio.querydsl.entityql.spec;

import org.junit.Test;
import pl.exsio.querydsl.entityql.QExporter;
import pl.exsio.querydsl.entityql.config.entity.*;

import static pl.exsio.querydsl.entityql.EntityQL.qEntity;

public class QExporterJavaITSpec {

    String pkgName = "pl.exsio.querydsl.entityql.config.entity.generated";

    String fileNamePattern = "Q%s.java";

    String destinationPath = getClass().getResource(".").getPath().replace("/", "\\").substring(1) +
            "..\\..\\..\\..\\..\\..\\..\\src\\test\\java";

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
