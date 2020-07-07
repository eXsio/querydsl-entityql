package pl.exsio.querydsl.entityql.groovy.runtime.it

import pl.exsio.querydsl.entityql.entity.scanner.runtime.Column
import pl.exsio.querydsl.entityql.entity.scanner.runtime.Table
import pl.exsio.querydsl.entityql.groovy.config.enums.by_name.UserTypeByName
import pl.exsio.querydsl.entityql.groovy.config.enums.by_ordinal.UserTypeByOrdinal
import spock.lang.Specification

abstract class RuntimeFixedTestBase extends Specification {

    public static final Table TABLE_ORDER_ITEMS = Table.of("ORDER_ITEMS", "", [
            Column.of(Long.class, "ORDER_ITEM_ID", false, true),
            Column.of(Long.class, "BOOK_ID", false, false),
            Column.of(Long.class, "ITEM_ORDER_ID", false, false),
            Column.of(Long.class, "QTY", false, false)
    ])

    public static final Table TABLE_USERS = Table.of("USERS", "", [
            Column.of(Long.class, "USER_ID", false, true),
            Column.of(String.class, "NAME", true, false),
            Column.of(Long.class, "ORDER_ID", true, false),
            Column.of(UserTypeByName.class, "TYPE_STR", false, false),
            Column.of(UserTypeByOrdinal.class, "TYPE_ORD", false, false),
            Column.of(String.class, "CREATED_BY", true, false),
            Column.of(Date.class, "CREATED_AT", true, false),
            Column.of(Boolean.class, "ENABLED", true, false),
    ])

    public static final Table TABLE_BOOKS = Table.of("BOOKS", "", [
            Column.of(Long.class, "BOOK_ID", false, true),
            Column.of(String.class, "NAME", true, false),
            Column.of(String.class, "DESC", true, false),
            Column.of(BigDecimal.class, "PRICE", true, false),
    ])

    public static final Table TABLE_ORDERS = Table.of("ORDERS", "", [
            Column.of(Long.class, "ORDER_ID", false, true),
            Column.of(Long.class, "USER_ID", false, false),
    ])

    public static final Table TABLE_UPLOADED_FILES = Table.of("UPLOADED_FILES", "", [
            Column.of(UUID.class, "FILE_ID", false, true),
            Column.of(byte[].class, "DATA", false, false),
    ])

    public static final Table TABLE_COUNTRY = Table.of("COUNTRY", "", [
            Column.of(Long.class, "ID", false, false),
            Column.of(String.class, "NAME", false, false),
    ])

    public static final Table TABLE_CITY = Table.of("CITY", "", [
            Column.of(Long.class, "ID", false, true),
            Column.of(String.class, "NAME", false, false),
    ])

}
