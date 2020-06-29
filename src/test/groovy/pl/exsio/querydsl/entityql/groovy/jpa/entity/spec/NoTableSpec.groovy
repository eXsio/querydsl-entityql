package pl.exsio.querydsl.entityql.groovy.jpa.entity.spec

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
public class NoTableSpec {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    public NoTableSpec() {
    }

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    String getString() {
        return string
    }

    void setString(String string) {
        this.string = string
    }

    BigDecimal getDecimalNumber() {
        return decimalNumber
    }

    void setDecimalNumber(BigDecimal decimalNumber) {
        this.decimalNumber = decimalNumber
    }

    BigDecimal getShortNumber() {
        return shortNumber
    }

    void setShortNumber(BigDecimal shortNumber) {
        this.shortNumber = shortNumber
    }

    LocalDate getDate() {
        return date
    }

    void setDate(LocalDate date) {
        this.date = date
    }

    LocalDateTime getDateTime() {
        return dateTime
    }

    void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime
    }

    Set<SubSpec> getSubUnits() {
        return subUnits
    }

    void setSubUnits(Set<SubSpec> subUnits) {
        this.subUnits = subUnits
    }

    void setShortNumber(Short shortNumber) {
        this.shortNumber = shortNumber
    }

    Integer getIntNumber() {
        return intNumber
    }

    void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber
    }
}
