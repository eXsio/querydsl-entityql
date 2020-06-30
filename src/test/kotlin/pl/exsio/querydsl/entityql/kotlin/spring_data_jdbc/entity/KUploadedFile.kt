package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.util.*

import javax.annotation.Nonnull

class KUploadedFile(@Id @Column("FILE_ID") var id: UUID, var data: ByteArray)
