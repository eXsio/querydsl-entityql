package pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.spec


import pl.exsio.querydsl.entityql.QExporter
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner
import pl.exsio.querydsl.entityql.entity.scanner.SpringDataJdbcQEntityScanner
import pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.UpperCaseWithUnderscoresNamingStrategy
import pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it.*
import spock.lang.Ignore
import spock.lang.Specification

import java.nio.file.Paths

import static pl.exsio.querydsl.entityql.EntityQL.qEntity

public class QExporterSpringDataJdbcITSpec extends Specification {

    String pkgName = "pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it.generated"

    String fileNamePattern = "Q%s.groovy"

    String destinationPath = Paths.get("src/test/groovy").toAbsolutePath()

    QExporter exporter = new QExporter();
    QEntityScanner scanner = new SpringDataJdbcQEntityScanner(new UpperCaseWithUnderscoresNamingStrategy());

    @Ignore
    def "should export integration q classes to static meta models"() {
        when:
        exporter.export(qEntity(Book, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(Group, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(GroupAdmin, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(Order, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(OrderItem, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(UploadedFile, scanner), fileNamePattern, pkgName, destinationPath)
        exporter.export(qEntity(User, scanner), fileNamePattern, pkgName, destinationPath)

        then:
        noExceptionThrown()
    }
}
