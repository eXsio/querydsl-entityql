package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

class KOrder(@Id @Column("ORDER_ID") var id: Long, var user: KUser<*>, var items: List<KOrderItem>)
