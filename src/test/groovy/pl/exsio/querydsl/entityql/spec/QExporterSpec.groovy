package pl.exsio.querydsl.entityql.spec

import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.QExporter
import pl.exsio.querydsl.entityql.config.entity.it.UploadedFile
import pl.exsio.querydsl.entityql.config.entity.it.User
import pl.exsio.querydsl.entityql.config.entity.spec.Spec
import pl.exsio.querydsl.entityql.config.entity.spec.SubSpec
import spock.lang.Specification

import static pl.exsio.querydsl.entityql.EntityQL.qEntity

public class QExporterSpec extends Specification {

    QExporter exporter = new QExporter();

    def "should export q class tyo static meta model"() {
        when:
        Q<Spec> spec = qEntity(Spec)
        Q<SubSpec> subSpec = qEntity(SubSpec)
        Q<UploadedFile> UploadedFile = qEntity(UploadedFile)
        Q<User> user = qEntity(User)
        exporter.export(spec, "Q%s.java", "pl.exsio.querydsl.entityql.config.entity.spec.generated", "e:\\tmp")
        exporter.export(subSpec, "Q%s.java", "pl.exsio.querydsl.entityql.config.entity.spec.generated", "e:\\tmp")
        exporter.export(UploadedFile, "Q%s.java", "pl.exsio.querydsl.entityql.config.entity.spec.generated", "e:\\tmp")
        exporter.export(user, "Q%s.java", "pl.exsio.querydsl.entityql.config.entity.spec.generated", "e:\\tmp")
        then:
        noExceptionThrown()
    }
}
