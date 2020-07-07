 package pl.exsio.querydsl.entityql.kotlin.jpa.entity.generated

 import com.querydsl.sql.PrimaryKey
 import pl.exsio.querydsl.entityql.QColumnMetadataFactory
 import pl.exsio.querydsl.entityql.QPathConfig
 import pl.exsio.querydsl.entityql.QPathFactory
 import pl.exsio.querydsl.entityql.QStaticModel
 import com.querydsl.sql.ForeignKey
 import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KUser
 import com.querydsl.core.types.dsl.NumberPath
 import com.querydsl.core.types.dsl.StringPath
 import pl.exsio.querydsl.entityql.kotlin.config.enums.by_name.KUserTypeByName
 import pl.exsio.querydsl.entityql.path.QEnumPath
 import pl.exsio.querydsl.entityql.kotlin.config.enums.by_ordinal.KUserTypeByOrdinal
 import pl.exsio.querydsl.entityql.path.QObjectPath
 import java.util.Date
 import com.querydsl.core.types.dsl.BooleanPath
 import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KOrder
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
 class QKUser : QStaticModel<KUser<*>> {

     companion object {
         val instance: QKUser = QKUser()
         val qKUser: QKUser = QKUser.instance
     }

     val id: NumberPath<Long> = run {
         val config = QPathConfig(Long::class.java, Long::class.java, "USER_ID", true, 1, -5)
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

     val typeStr: QEnumPath<KUserTypeByName> = run {
         val config = QPathConfig(KUserTypeByName::class.java, Enum::class.java, "TYPE_STR", false, 4, 12)
         val typeStr = QPathFactory.create<QEnumPath<KUserTypeByName>>(this, config)
         addMetadata(typeStr, QColumnMetadataFactory.create(config))
         this.columnsMap.put("typeStr", typeStr)
         typeStr
     }

     val typeOrd: QEnumPath<KUserTypeByOrdinal> = run {
         val config = QPathConfig(KUserTypeByOrdinal::class.java, Enum::class.java, "TYPE_ORD", false, 5, 12)
         val typeOrd = QPathFactory.create<QEnumPath<KUserTypeByOrdinal>>(this, config)
         addMetadata(typeOrd, QColumnMetadataFactory.create(config))
         this.columnsMap.put("typeOrd", typeOrd)
         typeOrd
     }

     val createdBy: QObjectPath<Any> = run {
         val config = QPathConfig(Object::class.java, Object::class.java, "CREATED_BY", true, 6, 1111)
         val createdBy = QPathFactory.create<QObjectPath<Any>>(this, config)
         addMetadata(createdBy, QColumnMetadataFactory.create(config))
         this.columnsMap.put("createdBy", createdBy)
         createdBy
     }

     val createdAt: QObjectPath<Date> = run {
         val config = QPathConfig(Date::class.java, Object::class.java, "CREATED_AT", true, 7, 1111)
         val createdAt = QPathFactory.create<QObjectPath<Date>>(this, config)
         addMetadata(createdAt, QColumnMetadataFactory.create(config))
         this.columnsMap.put("createdAt", createdAt)
         createdAt
     }

     val enabled: BooleanPath = run {
         val config = QPathConfig(Boolean::class.java, Boolean::class.java, "ENABLED", true, 8, 1111)
         val enabled = QPathFactory.create<BooleanPath>(this, config)
         addMetadata(enabled, QColumnMetadataFactory.create(config))
         this.columnsMap.put("enabled", enabled)
         enabled
     }

     val order: ForeignKey<KOrder> = run {
         val config0 = QPathConfig(Long::class.java, Long::class.java, "USER_ID", false, 3, -5)
         val order0 = QPathFactory.create<Path<*>>(this, config0)
         addMetadata(order0, QColumnMetadataFactory.create(config0))
         val order = this.createInvForeignKey<KOrder>(listOf(order0), listOf("USER_ID"))
         this.inverseJoinColumnsMap.put("order", order)
         order
      }

     val _primaryKey: PrimaryKey<KUser<*>> = run {
         val list = mutableListOf<Path<*>>(this.id)
         this.primaryKeyColumns = list
         this.createPrimaryKey(*list.toTypedArray())
     }

     constructor(): this("USERS")

     constructor(variable: String): super(KUser::class.java, variable, "", "USERS")
 } 