package pl.exsio.querydsl.entityql.groovy.jpa.spec

import pl.exsio.querydsl.entityql.QExporter
import pl.exsio.querydsl.entityql.groovy.jpa.entity.it.*
import spock.lang.Ignore
import spock.lang.Specification

import java.nio.file.Paths

import static pl.exsio.querydsl.entityql.EntityQL.qEntity

public class QExporterITSpec extends Specification {

    String pkgName = "pl.exsio.querydsl.entityql.groovy.jpa.entity.it.generated"

    String fileNamePattern = "Q%s.groovy"

    String destinationPath = Paths.get("src/test/groovy").toAbsolutePath().toString()

    QExporter exporter = new QExporter();

    @Ignore
    def "should export integration q classes to static meta models"() {
        when:
        exporter.export(qEntity(Book), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(CompositePk), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(CompositeFk), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(Group), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(GroupAdmin), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(Order), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(OrderItem), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(SingularPk), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(UploadedFile), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(User), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(UserGroup), fileNamePattern, pkgName, destinationPath)

        then:
        noExceptionThrown()
    }
}
