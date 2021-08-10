package pl.exsio.querydsl.entityql.kotlin.jpa.spec

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import pl.exsio.querydsl.entityql.EntityQL.qEntity
import pl.exsio.querydsl.entityql.QExporter
import pl.exsio.querydsl.entityql.entity.scanner.SpringDataJdbcQEntityScanner
import pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.*
import java.nio.file.Paths

class KQExporterITSpringDataJDBCSpec {

    val pkgName = "pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity.generated"

    val fileNamePattern = "Q%s.kt"

    val destinationPath = Paths.get("src/test/kotlin").toAbsolutePath().toString()

    val scanner = SpringDataJdbcQEntityScanner(KUpperCaseWithUnderscoresNamingStrategy())
    
    val exporter = QExporter()

    @Disabled
    @Test
    fun shouldExportIntegrationQClassesToStaticMetaModels() {
        exporter.export(qEntity(KBook::class.java, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KGroup::class.java, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KGroupAdmin::class.java, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KOrder::class.java, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KOrderItem::class.java, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KUploadedFile::class.java, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(KUser::class.java, scanner), fileNamePattern, pkgName, destinationPath)
    }
}
