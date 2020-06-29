package pl.exsio.querydsl.entityql.kotlin.config.dto

import pl.exsio.querydsl.entityql.kotlin.config.enums.by_name.KUserTypeByName

data class KUserDto(val id: Long, val name: String, val type: KUserTypeByName, val enabled: Boolean)
