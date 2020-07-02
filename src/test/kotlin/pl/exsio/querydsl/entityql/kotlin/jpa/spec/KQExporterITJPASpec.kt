package pl.exsio.querydsl.entityql.kotlin.jpa.spec

import org.junit.Ignore
import org.junit.Test
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.QExporter
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.*
import java.nio.file.Paths

class KQExporterITJPASpec {

    val pkgName = "pl.exsio.querydsl.entityql.kotlin.jpa.entity.generated"

    val fileNamePattern = "Q%s.kt"

    val destinationPath = Paths.get("src/test/kotlin").toAbsolutePath().toString()

    val exporter = QExporter()

    //@Ignore
    @Test
    fun shouldExportIntegrationQClassesToStaticMetaModels() {
        exporter.export(qEntity(KBook::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KCompositePk::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KCompositeFk::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KGroup::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KGroupAdmin::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KOrder::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KOrderItem::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KSingularPk::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KUploadedFile::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KUser::class.java), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KUserGroup::class.java), fileNamePattern, pkgName, destinationPath)
    }
}
