package pl.exsio.querydsl.entityql.kotlin.jpa.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "UPLOADED_FILES")
class KUploadedFile() {

    @Id
    @Column(name = "FILE_ID", nullable = false, columnDefinition = "UUID")
    @GeneratedValue
    var id: UUID? = null

    @Lob
    @Column(name = "DATA", nullable = false)
    var data:ByteArray? = null

   constructor(id: UUID, data: ByteArray) : this() {
       this.id = id
       this.data = data
   }
}
