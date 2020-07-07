package pl.exsio.querydsl.entityql.groovy.runtime.it


import org.springframework.beans.factory.annotation.Autowired
import pl.exsio.querydsl.entityql.entity.scanner.runtime.Table
import schemacrawler.schema.Catalog
import schemacrawler.schema.Column
import schemacrawler.schema.Schema
import schemacrawler.schemacrawler.SchemaCrawlerOptions
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder
import schemacrawler.utility.SchemaCrawlerUtility
import spock.lang.Specification

import javax.sql.DataSource

abstract class RuntimeDBTestBase extends Specification {

    @Autowired
    DataSource dataSource

    Map<String, Table> tables = [:]

    /**
     * We cannot use setupSpec due we can't access @Shared fields
     */
    def setup() {
        tables = exportMetadata(dataSource, "PUBLIC", "CAR")
                .collectEntries { [it.tableName, it] }
    }

    def exportMetadata(DataSource dataSource, String schemaFilter, String tableFilter) {
        SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.builder().toOptions()
        Catalog catalog = SchemaCrawlerUtility.getCatalog(dataSource.getConnection(), options)
        def schema = catalog.getSchemas().find { it.getName() =~ schemaFilter }
        convertTables(catalog, schema, tableFilter)
    }

    List<Table> convertTables(Catalog catalog, Schema schema, String tableFilter) {
        def tables = catalog.getTables(schema).findAll { it.getName() =~ tableFilter }
        tables.collect { table ->
            Table.of(
                    table.getName(),
                    schema.getName(),
                    convertColumns(table.getColumns())
            )
        }
    }

    List<pl.exsio.querydsl.entityql.entity.scanner.runtime.Column> convertColumns(List<Column> columns) {
        columns.collect { column ->
            pl.exsio.querydsl.entityql.entity.scanner.runtime.Column.of(
                    column.getColumnDataType().getJavaSqlType().getDefaultMappedClass(),
                    column.getName(),
                    column.isNullable(),
                    column.isPartOfPrimaryKey()
            )
        }
    }

}
