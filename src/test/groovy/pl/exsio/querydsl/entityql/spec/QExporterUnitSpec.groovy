package pl.exsio.querydsl.entityql.spec

import pl.exsio.querydsl.entityql.QExporter
import pl.exsio.querydsl.entityql.jpa.entity.spec.Spec
import pl.exsio.querydsl.entityql.jpa.entity.spec.SubSpec
import pl.exsio.querydsl.entityql.jpa.entity.spec.SubSpec2
import spock.lang.Specification

import static pl.exsio.querydsl.entityql.EntityQL.qEntity

public class QExporterUnitSpec extends Specification {

    String pkgName = "pl.exsio.querydsl.entityql.jpa.entity.spec.generated"

    String fileNamePattern = "Q%s.java"

    String destinationPath = System.getProperty("java.io.tmpdir")

    QExporter exporter = new QExporter();


    def "should export unit q classes to static meta models"() {
        when:
        exporter.export(qEntity(Spec), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(SubSpec), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(SubSpec2), fileNamePattern, pkgName, destinationPath)

        then:
        noExceptionThrown()
    }
}
