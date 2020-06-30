package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

class KGroup(@Id @Column("GROUP_ID") var id: Long, var name: String, var admin: KGroupAdmin?) {

    constructor(id: Long, name: String) : this(id, name, null)
}


