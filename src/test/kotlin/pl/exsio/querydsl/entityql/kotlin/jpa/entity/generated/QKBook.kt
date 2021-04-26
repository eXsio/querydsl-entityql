 package pl.exsio.querydsl.entityql.kotlin.jpa.entity.generated

 import com.querydsl.sql.PrimaryKey
 import pl.exsio.querydsl.entityql.QColumnMetadataFactory
 import pl.exsio.querydsl.entityql.QPathConfig
 import pl.exsio.querydsl.entityql.QPathFactory
 import pl.exsio.querydsl.entityql.QStaticModel
 import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KBook
 import com.querydsl.core.types.dsl.NumberPath
 import com.querydsl.core.types.dsl.StringPath
 import java.math.BigDecimal
 import javax.annotation.Generated
 import com.querydsl.core.types.Path
 

 /**
 *
 * This class was generated by EntityQL (https://github.com/eXsio/querydsl-entityql).
 * It is not recommended to make any changes to this class.
 * Any manual changes will be lost upon the next class generation.
 *
 */
 @Generated("pl.exsio.querydsl.entityql.QExporter")
 class QKBook : QStaticModel<KBook> {

     companion object {
         val instance: QKBook = QKBook()
         val qKBook: QKBook = QKBook.instance
     }

     val id: NumberPath<Long> = run {
         val config = QPathConfig(Long::class.java, Long::class.java, "BOOK_ID", true, 1, -5)
         val id = QPathFactory.create<NumberPath<Long>>(this, config)
         addMetadata(id, QColumnMetadataFactory.create(config))
         this.columnsMap.put("id", id)
         id
     }

     val name: StringPath = run {
         val config = QPathConfig(String::class.java, String::class.java, "NAME", true, 2, 12)
         val name = QPathFactory.create<StringPath>(this, config)
         addMetadata(name, QColumnMetadataFactory.create(config))
         this.columnsMap.put("name", name)
         name
     }

     val desc: StringPath = run {
         val config = QPathConfig(String::class.java, String::class.java, "DESC", true, 3, 2005)
         val desc = QPathFactory.create<StringPath>(this, config)
         addMetadata(desc, QColumnMetadataFactory.create(config))
         this.columnsMap.put("desc", desc)
         desc
     }

     val price: NumberPath<BigDecimal> = run {
         val config = QPathConfig(BigDecimal::class.java, BigDecimal::class.java, "PRICE", true, 4, 3)
         val price = QPathFactory.create<NumberPath<BigDecimal>>(this, config)
         addMetadata(price, QColumnMetadataFactory.create(config))
         this.columnsMap.put("price", price)
         price
     }

     val _primaryKey: PrimaryKey<KBook> = run {
         val list = mutableListOf<Path<*>>(this.id)
         this.primaryKeyColumns = list
         this.createPrimaryKey(*list.toTypedArray())
     }

     constructor(): this("BOOKS")

     constructor(variable: String): super(KBook::class.java, variable, "", "BOOKS")
 } 