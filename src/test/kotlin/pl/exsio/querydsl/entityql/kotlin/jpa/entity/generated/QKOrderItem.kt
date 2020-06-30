package pl.exsio.querydsl.entityql.kotlin.jpa.entity.generated

import com.querydsl.core.dml.StoreClause
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.sql.ForeignKey
import com.querydsl.sql.PrimaryKey
import java.util.ArrayList
import java.util.Arrays
import javax.annotation.Generated
import pl.exsio.querydsl.entityql.QColumnMetadataFactory
import pl.exsio.querydsl.entityql.QPathConfig
import pl.exsio.querydsl.entityql.QPathFactory
import pl.exsio.querydsl.entityql.QStaticModel
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KBook
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KOrder
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KOrderItem

/**
 *
 * This class was generated by EntityQL (https://github.com/eXsio/querydsl-entityql). It is not
 * recommended to make any changes to this class. Any manual changes will be lost upon the next
 * class generation.
 */
@Generated("pl.exsio.querydsl.entityql.QExporter")
class QKOrderItem : QStaticModel<KOrderItem> {

  companion object {
    val instance: QKOrderItem = QKOrderItem()
    val qKOrderItem: QKOrderItem = QKOrderItem.instance
  }

  lateinit var id: NumberPath<Long>

  lateinit var quantity: NumberPath<Long>

  lateinit var bookId: NumberPath<Long>

  lateinit var orderId: NumberPath<Long>

  lateinit var orderReferencedId: NumberPath<Long>

  lateinit var book: ForeignKey<KBook>

  lateinit var order: ForeignKey<KOrder>

  lateinit var orderReferenced: ForeignKey<KOrder>

  lateinit var _primaryKey: PrimaryKey<KOrderItem>

  constructor() : this("ORDER_ITEMS")

  constructor(variable: String) : super(KOrderItem::class.java, variable, "", "ORDER_ITEMS") {

    // id
    run {
      val config = QPathConfig(Long::class.java, Long::class.java, "ORDER_ITEM_ID", true, 1, -5)

      this.id = QPathFactory.create<NumberPath<Long>>(this, config)

      addMetadata(this.id, QColumnMetadataFactory.create(config))
      this.columnsMap.put("id", this.id)
    }

    // quantity
    run {
      val config = QPathConfig(Long::class.java, Long::class.java, "QTY", false, 5, -5)

      this.quantity = QPathFactory.create<NumberPath<Long>>(this, config)

      addMetadata(this.quantity, QColumnMetadataFactory.create(config))
      this.columnsMap.put("quantity", this.quantity)
    }

    // bookId
    run {
      val config = QPathConfig(Long::class.java, Long::class.java, "BOOK_ID", false, 2, -5)

      this.bookId = QPathFactory.create<NumberPath<Long>>(this, config)

      addMetadata(this.bookId, QColumnMetadataFactory.create(config))
      this.columnsMap.put("bookId", this.bookId)
    }

    // orderId
    run {
      val config = QPathConfig(Long::class.java, Long::class.java, "ITEM_ORDER_ID", false, 3, -5)

      this.orderId = QPathFactory.create<NumberPath<Long>>(this, config)

      addMetadata(this.orderId, QColumnMetadataFactory.create(config))
      this.columnsMap.put("orderId", this.orderId)
    }

    // orderReferencedId
    run {
      val config = QPathConfig(Long::class.java, Long::class.java, "ITEM_ORDER_ID", false, 4, -5)

      this.orderReferencedId = QPathFactory.create<NumberPath<Long>>(this, config)

      addMetadata(this.orderReferencedId, QColumnMetadataFactory.create(config))
      this.columnsMap.put("orderReferencedId", this.orderReferencedId)
    }

    // book
    run {
      this.book = this.createForeignKey<KBook>(this.bookId, "BOOK_ID")

      this.joinColumnsMap.put("book", this.book)
    }

    // order
    run {
      this.order = this.createForeignKey<KOrder>(this.orderId, "ORDER_ID")

      this.joinColumnsMap.put("order", this.order)
    }

    // orderReferenced
    run {
      this.orderReferenced = this.createForeignKey<KOrder>(this.orderReferencedId, "ORDER_ID")

      this.joinColumnsMap.put("orderReferenced", this.orderReferenced)
    }

    // _primaryKey
    run {
      val list = mutableListOf<Path<*>>(this.id)

      this.primaryKeyColumns = list
      this._primaryKey = this.createPrimaryKey(list.toTypedArray() as Path<*>)
    }
  }
}