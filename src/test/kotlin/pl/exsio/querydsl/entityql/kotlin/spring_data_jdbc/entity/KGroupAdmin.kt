package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

class KGroupAdmin( @Id @Column("GA_ID") var id: Long, var name: String)
