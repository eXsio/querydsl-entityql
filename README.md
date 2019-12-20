# QueryDSL EntityQL - Native Query builder for JPA

[![Build Status](https://travis-ci.org/eXsio/querydsl-entityql.svg?branch=master)](https://travis-ci.org/eXsio/querydsl-entityql)
[![codecov](https://codecov.io/gh/eXsio/querydsl-entityql/branch/master/graph/badge.svg)](https://codecov.io/gh/eXsio/querydsl-entityql)



## TL;DR

Have you ever had a situation where you had to perform a SQL operation that was not supported by JPA? 
The only solution was to create an ugly String with Native SQL, right? Well, not anymore!
With EntityQL you can create Native Queries using your own JPA Entities and a beautiful, fluent Java API. 




## Overview

EntityQL is a tool that is able to use JPA Entity mappings and create QueryDSL-SQL meta models on the fly.
Those Models can be then used to construct Native SQL Queries based on JPA mappings, using QueryDSL fluent API.


It works with QueryDSL-SQL, not QueryDSL-JPA. I will use the term QueryDSL in the context of QueryDSL-SQL.

## How it works

There is a special method ```EntityQL::qEntity``` that uses Reflection to gather all DDL information required to construct 
QueryDSL meta-model and to sucessfully perform all operations supported by QueryDSL. The scan occurs once per Entity class - 
the Annotation metadata is cached in memory for further reuse.

The meta-models are also created in memory, there is no code generation during compilation or runtime. The resulting instance
of ```Q``` class contains ```Maps``` containing the mappings between the Entity's field names and the corresponding
QueryDSL-specific models that are used for constructing SQL Queries.

Once we've obtained an instance of ```Q``` class, everything down the line is just plain QueryDSL API in motion. 
Please see the examples section to see how easy it is in practice.

## Use Cases

There are 2 primary use cases for EntityQL:

1) Supplementary add-on on top of existing JPA/Hibernate usage.

    The main strength of EntityQL is that it is capable of taking the preexisting JPA Entity mappings and construct Native Queries
    using QueryDSL API. JPQL and Criteria API are sufficient for most mundane data related tasks, but they fail miserably every time
    we need to perform some more complex SQL statemet (select from select and window functions being a good example). Using plain JPA
    would force us to either
    - use String - based Native SQL Queries
    - move the logic to the Database and use Stored Procedure
    - create inefficient workarounds in Java
    
    None of the above solutions are convenient and safe. EntityQL provides a way to circumvent those kinds of issues in a 
    modern, safe and readable way.
    
    QueryDSL (when properly configured) is able to work in the scope of the same transaction as the Entity Manager, so we can
    even mix and match the usages within the same transactions.

2) Lightweight alternative to JPA/Hibernate.

    EntityQL is a perfect fit for persistence layer for users who:
    - like the JPA's style of code-first database schema management
    - would like to retain the abstraction layer making the persistence code more portable (QueryDSL supports all major databases)
    - likes the easy testing in in-memory databases thanks to Hibernate's schema generation features
    - doesn't need/want all the fireworks offered by Hibernate (like dirty checking, auto flushing, cascades etc)
    - would like to have 100% control over the executed SQL statements
    - needs SQL features unavailable in JPA, but supported by QueryDSL (like window functions)
    - wants to have unbeatable persistence performance (QueryDSL is orders of magnitude faster than Hibernate as it has minimal abstractions and works directly on JDBC level)

    EntityQL is just a translation layer between JPA mappings and QueryDSL. QueryDSL is perfectly capable to handle all DML statements.

## Examples

Using the following Entities:

```java

@Entity
@Table(name = "BOOKS")
public class Book {

    @Id
    @Column(name = "BOOK_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME", unique = true)
    private String name;


    @Column(name = "DESC", nullable = true, columnDefinition = "CLOB")
    private String desc;

    @Column(name = "PRICE")
    private BigDecimal price;
}

@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {

    @Id
    @Column(name = "ORDER_ID")
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
}

@Entity
@Table(name = "ORDER_ITEMS")
public class OrderItem implements Serializable {

    @Id
    @Column(name = "ORDER_ITEM_ID")
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @Column(name = "QTY", nullable = false)
    private Long quantity;
}

```

We can construct execute the following example SQL Queries:

- simple select with projection:

```java
Q<Book> book = qEntity(Book.class);

List<BookDto> books = queryFactory.query()
                .select(
                        constructor(
                                BookDto.class,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(book).fetch();

```

- joins with 'on' clause:
```java
 Q<Book> book = qEntity(Book.class);
 Q<Order> order = qEntity(Order.class);
 Q<OrderItem> orderItem = qEntity(OrderItem.class);

List<BookDto> books = queryFactory.query()
                .select(
                        constructor(
                                BookDto.class,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(book)
                .innerJoin(orderItem).on(orderItem.longNumber("book").eq(book.longNumber("id")))
                .innerJoin(order).on(orderItem.longNumber("order").eq(order.longNumber("id")))
                .where(order.longNumber("id").eq(1L))
                .fetch()

```

- joins with Foreign Keys:
```java
 Q<Book> book = qEntity(Book.class);
 Q<Order> order = qEntity(Order.class);
 Q<OrderItem> orderItem = qEntity(OrderItem.class);

List<BookDto> books = queryFactory.query()
                .select(
                        constructor(
                                BookDto.class,
                                book.longNumber("id"),
                                book.string("name"),
                                book.string("desc"),
                                book.decimalNumber("price")
                        ))
                .from(orderItem)
                .innerJoin(orderItem.<Book> joinColumn("book"), book)
                .innerJoin(orderItem.<Order> joinColumn("order"), order)
                .where(order.longNumber("id").eq(2L))
                .fetch()
```

- DTO Projections with simple Column list:
```java
 Q<Book> book = qEntity(Book.class);

 List<BookDto> books = queryFactory.query()
                .select(
                        dto(BookDto.class, book.columns("id", "name", "desc", "price"))
                )
                .from(book)
                .fetch();

```

- nested Select clauses:
```java
 Q<Book> book = qEntity(Book.class);
 Q<Order> order = qEntity(Order.class);
 Q<OrderItem> orderItem = qEntity(OrderItem.class);

Long count = queryFactory.select(count())
                .from(
                        select(order.longNumber("user"))
                                .from(orderItem)
                                .innerJoin(orderItem.<Book> joinColumn("book"), book)
                                .innerJoin(orderItem.<Order> joinColumn("order"), order)
                                .where(book.decimalNumber("price").gt(new BigDecimal("80")))
                ).fetchOne();

```

- the usual DML statements:

```java
Q<Book> book = qEntity(Book.class);

queryFactory.insert(book)
                .set(book.longNumber("id"), 10L)
                .set(book.string("name"), "newBook")
                .set(book.decimalNumber("price"), BigDecimal.ONE)
                .execute();

queryFactory.update(book)
                .set(book.string("name"), "updatedBook")
                .set(book.decimalNumber("price"), BigDecimal.ONE)
                .where(book.longNumber("id").eq(9L))
                .execute();
```

- Simplified DML statements:

```java
Q<Book> book = qEntity(Book.class);

book.set(
        queryFactory.insert(book),
        "id", 11L,
        "name", "newBook2",
        "price", BigDecimal.ONE
).execute();


SQLUpdateClause update = queryFactory.update(book)
                .where(book.longNumber("id").eq(9L))

book.set(update,
        "name", "updatedBook",
        "price", BigDecimal.ONE
).execute()

```

If you want to see more examples, please explore the integration test suite.

## Installation
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.eXsio</groupId>
    <artifactId>querydsl-entityql</artifactId>
    <version>1.0.0</version>
</dependency>
```
## Configuration

There is nothing to be configured especially for EntityQL. All you need to have is configured ```SqlQueryFacotory``` and - 
inf you want to use Hibernate's schema generation - also configured Hibernate.

You can optionally use the provided ```EntityQlQueryFactory``` that is preconfigured to:
- work seamlessly with Spring's transaction management - uses ```DataSourceUtils``` to obtain ```Connection``` bound to active Transaction,
  autocloses the ```Connection``` if no in avtive Transsaction scope
- autoregisters all Enum types with QueryDSL
- autoregisters Boolean and UUID data types

In order to do that you will need to add additional dependencies:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.2.2.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.reflections</groupId>
    <artifactId>reflections</artifactId>
    <version>0.9.11</version>
</dependency>
```


## Limitations and restrictions

All the limitations revolve around wheter we have all the data needed to construct the meta-models. 
Hibernate contains a lot of magical features like auto-generation of table and column names, mapping columns to ```Map``` etc.
To work properly, EntityQL needs to work with well-formatted and completely described Entities.

 - Entity must have a valid ```@Table``` Annotation containing the Table name and (optionally) Schema name
 - Only fields containing ```@Column``` or ```@JoinColumn``` Annotations will be visible to EntityQL
 - When dealing with bidirectional mappings, only the sides that actually contain columns (```@JoinColumn```) will be supported,
   other sides will be ignored (```@OneToMany``` and the reversed ```@OneToOne```)
 - ```@JoinTable``` Annotation is not supported. If you want to use EntityQL with ```@ManyToMany``` mapping, 
    you can create an ```@Immutable @Entity``` that matches the table configured in ```@JoinTable```, for example:
    
    
```java
    
    @ManyToMany
        @JoinTable(
                name = "USERS_GROUPS",
                joinColumns = @JoinColumn(name = "GROUP_ID"),
                inverseJoinColumns = @JoinColumn(name = "USER_ID")
        )
        private Set<User> users;
```
can be supported by:
```java
@Entity
@Immutable
@Table(name = "USERS_GROUPS")
public class UserGroup implements Serializable {

    @Id
    @Column(name = "GROUP_ID", nullable = false)
    private Long groupId;

    @Id
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
}

```

## Support

Although this is a project I'm working on in my spare time, I try to fix any issues as soon as I can. If you nave a feature request that could prove useful I will also consider adding it in the shortest possible time.

## BUGS

If You find any bugs, feel free to submit PR or create an issue on GitHub: https://github.com/eXsio/querydsl-entityql
