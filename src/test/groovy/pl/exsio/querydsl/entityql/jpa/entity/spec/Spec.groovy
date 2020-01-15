package pl.exsio.querydsl.entityql.jpa.entity.spec

import org.hibernate.annotations.Type

import javax.persistence.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "SPECS")
public class Spec<T> {

    @Id
    @Column(name = "SPEC_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "STRING")
    private String string;

    @Column(name = "DEC")
    private BigDecimal decimalNumber;

    @Column(name = "SHRT")
    private Short shortNumber;

    @Column(name = "BTE")
    private Byte byteNumber;

    @Column(name = "DBL")
    private Double doubleNumber;

    @Column(name = "FLT")
    private Float floatNumber;

    @Column(name = "BIGINTEGER")
    private BigInteger bigintNumber;

    @Column(name = "DT")
    private LocalDate date;

    @Column(name = "DTT")
    private LocalDateTime dateTime;

    @Column(name = "INST")
    private Instant instant;

    @Column(name = "TIME")
    private LocalTime time;

    @Column(name = "TIMESTAMP")
    private Timestamp timestamp;

    @Column(name = "GEN", columnDefinition = "varchar(15)")
    @Type(type = "java.lang.String")
    private T generic;

    @OneToMany(mappedBy = "spec", cascade = CascadeType.ALL)
    private Set<SubSpec> subSpecs;

    @OneToMany
    @JoinColumn(name = "SPEC_ID", nullable = true)
    private Set<SubSpec2> subSpecs2;

    @Column(name = "INT")
    private Integer intNumber;




    public Spec() {
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
