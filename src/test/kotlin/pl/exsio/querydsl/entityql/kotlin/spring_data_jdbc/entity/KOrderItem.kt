package pl.exsio.querydsl.entityql.kotlin.spring_data_jdbc.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

class KOrderItem(@Id @Column("ORDER_ITEM_ID") var id: Long,
                 @Column("BOOK_ID") var book: KBook,
                 @Column("ITEM_ORDER_ID") var order: KOrder,
                 @Column("QTY") var quantity: Long
)

