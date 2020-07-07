package pl.exsio.querydsl.entityql.groovy.runtime.dto


import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString(includeFields = true)
@TupleConstructor(includeFields = true)
class Car {

    public Long id
    public String name
    public Long typeId
    public String color
    public BigDecimal price
    public Integer amount

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Car car = (Car) o

        if (amount != car.amount) return false
        if (color != car.color) return false
        if (id != car.id) return false
        if (name != car.name) return false
        if (price != car.price) return false
        if (typeId != car.typeId) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (name != null ? name.hashCode() : 0)
        result = 31 * result + (typeId != null ? typeId.hashCode() : 0)
        result = 31 * result + (color != null ? color.hashCode() : 0)
        result = 31 * result + (price != null ? price.hashCode() : 0)
        result = 31 * result + (amount != null ? amount.hashCode() : 0)
        return result
    }

}
