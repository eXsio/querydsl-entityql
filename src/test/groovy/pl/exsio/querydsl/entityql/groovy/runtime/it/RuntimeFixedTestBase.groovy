package pl.exsio.querydsl.entityql.groovy.runtime.it

import pl.exsio.querydsl.entityql.entity.scanner.runtime.QRuntimeColumn
import pl.exsio.querydsl.entityql.entity.scanner.runtime.QRuntimeTable
import pl.exsio.querydsl.entityql.groovy.config.enums.by_name.UserTypeByName
import pl.exsio.querydsl.entityql.groovy.config.enums.by_ordinal.UserTypeByOrdinal
import spock.lang.Specification

abstract class RuntimeFixedTestBase extends Specification {

    public static final QRuntimeTable TABLE_ORDER_ITEMS = QRuntimeTable.of("ORDER_ITEMS", "", [
            QRuntimeColumn.of(Long.class, "ORDER_ITEM_ID", false, true),
            QRuntimeColumn.of(Long.class, "BOOK_ID", false, false),
            QRuntimeColumn.of(Long.class, "ITEM_ORDER_ID", false, false),
            QRuntimeColumn.of(Long.class, "QTY", false, false)
    ])

    public static final QRuntimeTable TABLE_USERS = QRuntimeTable.of("USERS", "", [
            QRuntimeColumn.of(Long.class, "USER_ID", false, true),
            QRuntimeColumn.of(String.class, "NAME", true, false),
            QRuntimeColumn.of(Long.class, "ORDER_ID", true, false),
            QRuntimeColumn.of(UserTypeByName.class, "TYPE_STR", false, false),
            QRuntimeColumn.of(UserTypeByOrdinal.class, "TYPE_ORD", false, false),
            QRuntimeColumn.of(String.class, "CREATED_BY", true, false),
            QRuntimeColumn.of(Date.class, "CREATED_AT", true, false),
            QRuntimeColumn.of(Boolean.class, "ENABLED", true, false),
    ])

    public static final QRuntimeTable TABLE_BOOKS = QRuntimeTable.of("BOOKS", "", [
            QRuntimeColumn.of(Long.class, "BOOK_ID", false, true),
            QRuntimeColumn.of(String.class, "NAME", true, false),
            QRuntimeColumn.of(String.class, "DESC", true, false),
            QRuntimeColumn.of(BigDecimal.class, "PRICE", true, false),
    ])

    public static final QRuntimeTable TABLE_ORDERS = QRuntimeTable.of("ORDERS", "", [
            QRuntimeColumn.of(Long.class, "ORDER_ID", false, true),
            QRuntimeColumn.of(Long.class, "USER_ID", false, false),
    ])

    public static final QRuntimeTable TABLE_UPLOADED_FILES = QRuntimeTable.of("UPLOADED_FILES", "", [
            QRuntimeColumn.of(UUID.class, "FILE_ID", false, true),
            QRuntimeColumn.of(byte[].class, "DATA", false, false),
    ])

    public static final QRuntimeTable TABLE_COUNTRY = QRuntimeTable.of("COUNTRY", "", [
            QRuntimeColumn.of(Long.class, "ID", false, false),
            QRuntimeColumn.of(String.class, "NAME", false, false),
    ])

    public static final QRuntimeTable TABLE_CITY = QRuntimeTable.of("CITY", "", [
            QRuntimeColumn.of(Long.class, "ID", false, true),
            QRuntimeColumn.of(String.class, "NAME", false, false),
    ])

}
