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
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KOrder
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KOrderItem
import pl.exsio.querydsl.entityql.kotlin.jpa.entity.KUser

/**
 *
 * This class was generated by EntityQL (https://github.com/eXsio/querydsl-entityql). It is not
 * recommended to make any changes to this class. Any manual changes will be lost upon the next
 * class generation.
 */
@Generated("pl.exsio.querydsl.entityql.QExporter")
class QKOrder : QStaticModel<KOrder> {

  companion object {
    val instance: QKOrder = QKOrder()
    val qKOrder: QKOrder = QKOrder.instance
  }

  lateinit var id: NumberPath<Long>

  lateinit var userId: NumberPath<Long>

  lateinit var user: ForeignKey<KUser>

  lateinit var items: ForeignKey<KOrderItem>

  lateinit var itemsReferenced: ForeignKey<KOrderItem>

  lateinit var _primaryKey: PrimaryKey<KOrder>

  constructor() : this("ORDERS")

  constructor(variable: String) : super(KOrder::class.java, variable, "", "ORDERS") {

    // id
    run {
      val config = QPathConfig(Long::class.java, Long::class.java, "ORDER_ID", true, 1, -5)

      this.id = QPathFactory.create<NumberPath<Long>>(this, config)

      addMetadata(this.id, QColumnMetadataFactory.create(config))
      this.columnsMap.put("id", this.id)
    }

    // userId
    run {
      val config = QPathConfig(Long::class.java, Long::class.java, "USER_ID", false, 2, -5)

      this.userId = QPathFactory.create<NumberPath<Long>>(this, config)

      addMetadata(this.userId, QColumnMetadataFactory.create(config))
      this.columnsMap.put("userId", this.userId)
    }

    // user
    run {
      this.user = this.createForeignKey<KUser>(this.userId, "USER_ID")
      this.joinColumnsMap.put("user", this.user)
    }

    // items
    run {
      val config0 = QPathConfig(Long::class.java, Long::class.java, "ORDER_ID", false, 3, -5)

      val items0 = QPathFactory.create<Path<*>>(this, config0)
      addMetadata(items0, QColumnMetadataFactory.create(config0))

      this.items = this.createInvForeignKey<KOrderItem>(listOf(items0), listOf("ITEM_ORDER_ID"))

      this.inverseJoinColumnsMap.put("items", this.items)
    }

    // itemsReferenced
    run {
      val config0 = QPathConfig(Long::class.java, Long::class.java, "ORDER_ID", false, 4, -5)

      val itemsReferenced0 = QPathFactory.create<Path<*>>(this, config0)
      addMetadata(itemsReferenced0, QColumnMetadataFactory.create(config0))

      this.itemsReferenced =
          this.createInvForeignKey<KOrderItem>(listOf(itemsReferenced0), listOf("ITEM_ORDER_ID"))

      this.inverseJoinColumnsMap.put("itemsReferenced", this.itemsReferenced)
    }

    // _primaryKey
    run {
      val list = mutableListOf<Path<*>>(this.id)

      this.primaryKeyColumns = list
      this._primaryKey = this.createPrimaryKey(list.toTypedArray() as Path<*>)
    }
  }
}
