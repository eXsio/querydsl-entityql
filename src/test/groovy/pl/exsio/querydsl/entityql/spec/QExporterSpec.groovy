package pl.exsio.querydsl.entityql.spec


import pl.exsio.querydsl.entityql.QExporter
import pl.exsio.querydsl.entityql.config.entity.it.*
import spock.lang.Ignore
import spock.lang.Specification

import static pl.exsio.querydsl.entityql.EntityQL.qEntity

public class QExporterSpec extends Specification {

    String pkgName = "pl.exsio.querydsl.entityql.config.entity.it.generated"

    String fileNamePattern = "Q%s.groovy"

    String destinationPath = getClass().getResource(".").getPath().replace("/", "\\").substring(1) +
            "..\\..\\..\\..\\..\\..\\..\\src\\test\\groovy\\pl\\exsio\\querydsl\\entityql\\config\\entity\\it\\generated"

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
