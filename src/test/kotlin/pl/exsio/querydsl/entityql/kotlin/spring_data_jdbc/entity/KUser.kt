package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import pl.exsio.querydsl.entityql.groovy.config.enums.by_name.UserTypeByName
import pl.exsio.querydsl.entityql.groovy.config.enums.by_ordinal.UserTypeByOrdinal
import pl.exsio.querydsl.entityql.kotlin.config.enums.by_name.KUserTypeByName
import pl.exsio.querydsl.entityql.kotlin.config.enums.by_ordinal.KUserTypeByOrdinal
import java.util.*

class KUser<T>(@Id @Column("USER_ID") var id: Long,
               var name: String,
               var order: KOrder,
               var typeStr: KUserTypeByName,
               var typeOrd: KUserTypeByOrdinal,
               var createdBy: T,
               var createdAt: Date,
               var enabled: Boolean
)