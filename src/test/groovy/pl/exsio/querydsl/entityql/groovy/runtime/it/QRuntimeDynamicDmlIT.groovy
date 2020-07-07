package pl.exsio.querydsl.entityql.groovy.runtime.it

import com.querydsl.core.Tuple
import com.querydsl.sql.SQLQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.exsio.querydsl.entityql.Q
import pl.exsio.querydsl.entityql.groovy.config.SpringContext
import pl.exsio.querydsl.entityql.groovy.runtime.dto.Car
import pl.exsio.querydsl.entityql.type.QRuntimeProjections

import static com.querydsl.core.types.Projections.constructor
import static pl.exsio.querydsl.entityql.EntityQL.qEntity

@ContextConfiguration(classes = [SpringContext])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class QRuntimeDynamicDmlIT extends RuntimeDBTestBase {

    @Autowired
    SQLQueryFactory queryFactory


    def "should get database metadata at runtime and make select from given table"() {
        given:
        Q<Tuple> car = qEntity(tables.get("CAR"))

        when:
        List<Map<String, ?>> cars = queryFactory.query()
                .select(
                    QRuntimeProjections.map(
                            car.all()
                    ))
                .from(car)
                .fetch()

        then:
        cars.size() >= 4
        cars*.name.containsAll(["Lightning McQueen", "Optimus Prime", "The Batmobile", "The Flintstones Flintmobile"])
    }

    def "should get all rows from table at runtime"() {
        given:
        Q<Tuple> car = qEntity(tables.get("CAR"))

        when:
        List<Car> cars = queryFactory.query()
                .select(
                    constructor(
                            Car,
                            car.all()
                    ))
                .from(car)
                .fetch()

        then:
        cars.size() == 4
        cars*.color.containsAll(["RED", "RED+BLUE", "BLACK", "?"])
    }

    def "should get all rows from table based on a Column / ON Join at runtime"() {
        given:
        Q<Tuple> car = qEntity(tables.get("CAR"))
        Q<Tuple> carType = qEntity(tables.get("CAR_TYPE"))

        when:
        List<Map<String, ?>> cars = queryFactory.query()
                .select(
                    QRuntimeProjections.map(
                            car.all()
                    ))
                .from(car)
                .innerJoin(carType).on(carType.longNumber("id").eq(car.longNumber("typeId")))
                .where(carType.string("name").eq('TANK'))
                .fetch()

        then:
        cars.size() == 1
        cars[0] == ['amount': 2, 'color': 'BLACK', 'price': new BigDecimal('3.00'), 'name': 'The Batmobile', 'typeId': 6, id: 3]
    }

    def "should correctly insert new row at runtime"() {
        given:
        Q<Tuple> car = qEntity(tables.get("CAR"))

        when:
        queryFactory.insert(car)
                .set(car.string("name"), "Ecto-1")
                .set(car.longNumber("typeId"), 2L)
                .set(car.string("color"), "WHITE")
                .set(car.decimalNumber("price"), BigDecimal.TEN)
                .set(car.intNumber("amount"), 4)
                .execute()

        and:
        def actualResult = queryFactory.query()
                .select(
                    constructor(
                            Car,
                            car.all()
                    ))
                .from(car)
                .where(car.string("name").eq("Ecto-1"))
                .fetchOne()

        then:
        actualResult == new Car('amount': 4, 'color': 'WHITE', 'price': new BigDecimal('10.00'), 'name': 'Ecto-1', 'typeId': 2, id: 5)
    }

}
