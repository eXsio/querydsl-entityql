package pl.exsio.querydsl.entityql.groovy.runtime.spec

import com.querydsl.core.Tuple
import com.querydsl.core.types.Path
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.QFactory
import pl.exsio.querydsl.entityql.entity.metadata.QEntityJoinColumnMetadata
import pl.exsio.querydsl.entityql.entity.scanner.QRuntimeTableScanner
import pl.exsio.querydsl.entityql.entity.scanner.runtime.UnderscoreToCamelStrategyQRuntimeNamingStrategy
import pl.exsio.querydsl.entityql.ex.MissingIdException
import pl.exsio.querydsl.entityql.ex.UnsupportedScannerException
import pl.exsio.querydsl.entityql.groovy.runtime.it.RuntimeFixedTestBase
import pl.exsio.querydsl.entityql.type.QRuntimeMap
import pl.exsio.querydsl.entityql.type.QRuntimeProjections

import static pl.exsio.querydsl.entityql.EntityQL.qEntity

class QDynamicSpec extends RuntimeFixedTestBase {

    def "should correctly throw exception when table doesnt contain primary key"() {
        when:
        Q<Tuple> spec = qEntity(TABLE_COUNTRY)

        then:
        thrown MissingIdException
    }

    def "should throw exception when creating join metadata"() {
        given:
        def qFactory = QFactory.get(TABLE_CITY, new QRuntimeTableScanner(UnderscoreToCamelStrategyQRuntimeNamingStrategy.getInstance()))
        qFactory.metadata.joinColumns << new QEntityJoinColumnMetadata(
                String.class,
                "countryId",
                "id",
                true,
                0)

        when:
        qFactory.create(true)

        then:
        thrown UnsupportedScannerException
    }

    def "should correctly convert fieldName to column"() {
        given:
        def strategy = UnderscoreToCamelStrategyQRuntimeNamingStrategy.getInstance()

        when:
        def actualResult = strategy.getFieldName(column)

        then:
        actualResult == field

        where:
        column        | field
        'customer_id' | 'customerId'
        'CUSTOMER_ID' | 'customerId'
        'CustomeR_Id' | 'customerId'
        'cUSTOMEr_ID' | 'customerId'
    }

    def "should correctly convert column to fieldName"() {
        given:
        def strategy = UnderscoreToCamelStrategyQRuntimeNamingStrategy.getInstance()

        when:
        def actualResult = strategy.getColumnName(field)

        then:
        actualResult == column

        where:
        field        | column
        'customerId' | 'CUSTOMER_ID'
        'CustomerId' | 'CUSTOMER_ID'
        'customerid' | 'CUSTOMERID'
    }

    def "test for jacoco coverage"() {
        when:
        def simpleMap = new QRuntimeMap(null)
        def result = new QRuntimeMap(null).equals(new QRuntimeMap(null))
        def result2 = simpleMap.equals(simpleMap)
        def result3 = !simpleMap.equals(1L)

        then:
        result && result2 && result3
    }

    def "test for another jacoco coverage"() {
        when:
        Q<Tuple> orderItems = qEntity(TABLE_ORDER_ITEMS)
        Q<Tuple> orders = qEntity(TABLE_ORDERS)

        def simpleMap = QRuntimeProjections.map(
                orderItems.all(),
                [orders.longNumber("userId")] as Path<?>[]
        )

        then:
        simpleMap != null
    }

}
