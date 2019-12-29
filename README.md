# QueryDSL EntityQL - Native Query builder for JPA

[![Build Status](https://travis-ci.org/eXsio/querydsl-entityql.svg?branch=master)](https://travis-ci.org/eXsio/querydsl-entityql)
[![codecov](https://codecov.io/gh/eXsio/querydsl-entityql/branch/master/graph/badge.svg)](https://codecov.io/gh/eXsio/querydsl-entityql)



## TL;DR

Have you ever had a situation where you had to perform a SQL operation that was not supported by JPA? 
The only solution was to create an ugly String with Native SQL, right? Well, not anymore!
With EntityQL you can create Native Queries using your own JPA Entities and a beautiful, fluent Java API. 

#### Quick example:

```java

 //create ad-hoc QueryDSL Models out of your Entity classes
 Q<Book> book = qEntity(Book.class); 
 Q<Order> order = qEntity(Order.class);
 Q<OrderItem> orderItem = qEntity(OrderItem.class);

//use them by creating and executing a Native Query using QueryDSL API
Long count = queryFactory.select(count())
                .from(
                        select(
                                 book.string("name"), 
                                 order.longNumber("id")
                        )
                        .from(orderItem)
                        .innerJoin(orderItem.<Book> joinColumn("book"), book)
                        .innerJoin(orderItem.<Order> joinColumn("order"), order)
                        .where(book.decimalNumber("price").gt(new BigDecimal("80")))
                        .groupBy(book.longNumber("category")) 
                ).fetchOne();

```


Interested? Keep on reading! 

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

## QueryDSL SQL Features

All of the QueryDSL-SQL features are described here: http://www.querydsl.com/static/querydsl/4.2.1/reference/html_single/#d0e1067

## How does the EntityQL differ from...

1) **Hibernate / JPA in general** - EntityQL uses Entities only  as source of DDL information necessary to construct Native SQL that is executed against JDBC Connection. 
There is no Persistence Context, no Entity Manager, no L1/L2/L3 cache, no Dirty Checking, no Cascades. That makes EntityQL offer less "magic" features, but at the same time
makes it orders of magnitude faster than Hibernate.
 
2) **Hibernate / JPA Native Queries** - Hibernate offers Native SQL support only in a form of Strings containing full SQL queries. EntityQL offers full fluent QueryDSL Java API
that is way more safe and convenient to use, not to mention it is fully portable. QueryDSL has its form of Dialects that translates the query built with Java API to the SQL Dialect that matches
your Database.

3) **QueryDSL-JPA in general** - the JPA module is just an additional layer on top of JPA/Hibernate. All of the queries built with its API are translated into JPQL. 
Although QueryDSL-JPA API is lightyears ahead of ugly, unreadable Criteria API or error-prone JPQL Strings, it suffers from the same limited SQL operations it can support as the original JPA.

4) **QueryDSL-JPA JpaSqlQuery class** - the JPA module has the ability to construct Native SQL with Java API. The only limitation (at least for some) is that you need to generate both JPA
Meta Models (Q-classes created from your Entity Mappings) and SQL Meta Models (S-classes created by reverse engineering your Database Schema). EntityQL doesn't require
any static code generation, the Meta Models are generated on the fly and cached in memory for further reuse. 

5) **Vanilla QueryDSL-SQL and/or JOOQ** - both of the frameworks are offering similar feature sets and both rely on generating Static Meta Model by reverse engineering 
your Schema. That is a big advantage for some developers, but it has its own list of shortcomings. Their workflows demand that you create your Database Schema before your code.
Most of Java developers (especially ones used to dealing with JPA) like to create their Schema in a form of JPA Entities first, and then export them to Database. Also Static Meta Models, although
offer more type-safety, are tiresome to generate, especially in a multi-branch project. EntityQL lets you keep your Schema management in Java Code, allows the same level of integration testing 
as Hibernate (you can still use Hibernate to generate your test in-memory H2 Database) and at the same time offers convenient, fluent Java API that constructs Native Queries 
and runs them directly on JDBC level, making whole thing extremely fast.

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
    <version>1.0.2</version>
</dependency>

<!-- dependencies of EntityQL. JPA API can be skipped if you're using hibernate-core. -->
<dependency>
    <groupId>org.hibernate.javax.persistence</groupId>
    <artifactId>hibernate-jpa-2.1-api</artifactId>
    <version>1.0.2.Final</version>
</dependency>
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-sql</artifactId>
    <version>4.2.2</version>
</dependency>

```


## Configuration

There is nothing to be configured especially for EntityQL. All you need to have is configured ```SqlQueryFacotory``` and - 
if you want to use Hibernate's schema generation - also configured Hibernate.

You can optionally use the provided ```EntityQlQueryFactory``` that is preconfigured to:
- work seamlessly with Spring via the ```DataSourceUtils``` and ```SpringExceptionTranslator```
- auto-registers all Enum types with QueryDSL
- auto-registers Boolean and UUID data types

In order to do that you will need to add additional dependencies:

```xml
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-sql-spring</artifactId>
    <version>4.2.2</version>
</dependency>
<dependency>
    <groupId>org.reflections</groupId>
    <artifactId>reflections</artifactId>
    <version>0.9.11</version>
</dependency>
```

## Spring Configuration

1. Use the base and additional maven dependencies from the above section
2. Configure QueryDSL:

```java

        @Bean
        public SQLTemplates sqlTemplates() {
            //choose the implementation that matches your database engine
            return new H2Templates(); 
        }
    
        @Bean
        public SQLQueryFactory queryFactory(DataSource dataSource, SQLTemplates sqlTemplates) {
            //last param is an optional varargs String with all the java.lang.Enum packages that you use in your Entities
            return new EntityQlQueryFactory(new Configuration(sqlTemplates), dataSource, "your.enums.package");
        }

```

## Limitations and restrictions

EntityQL was created with 2 main principles in mind: simplicity and explicitness. 
All the limitations revolve around whether we have all the data needed to construct the meta-models. 

Hibernate contains a lot of magical features like auto-generation of table and column names, mapping columns to ```Map``` etc.
To work properly, EntityQL needs to work with well-formatted and completely/explicitly described Entities.

 - Entity must have a valid ```@Table``` Annotation containing the Table name and (optionally) Schema name
 - Only fields containing ```@Column``` or ```@JoinColumn``` Annotations will be visible to EntityQL
 - When dealing with bidirectional mappings, only the sides that actually contain columns (```@JoinColumn```) will be supported,
   other sides will be ignored (```@OneToMany``` and the reversed ```@OneToOne```)
 - In order to use Java Enums in queries, Enum classes have to be registered with QueryDSL's ```Configuration::register```
   using ```EnumType```. Alternatively you can use the provided ```EntityQlQueryFactory```. It will register all Enums from 
   the packages you want. 
 - In order to use UUIDs, you have to register ```UtilUUIDType``` with QueryDSL's ```Configuration::register```
 - In order to use Booleans you have to register ```BooleanType``` with QueryDSL's ```Configuration::register```
 - Unidirectional ```@OneToMany``` is not supported. Even though it contains a ```@JoinColumn``` mapping, that mapping applies to the target Entity,
   not to the one we're currently using. In such case you have to refactor your Entity to contain a Bidirectional ```@OneToMany```.
   
   Mapping like this:
   
   ```java
       @OneToMany
       @JoinColumn(name = "Y_ID", nullable = true)
       private Set<X> xSet;
   ```
   
   has to be refactored into:
   
   ```java
      @OneToMany(mappedBy = "y")
      private Set<X> xSet;
   ```
   
   and then on the other side we need to create a reverse mapping with a ```@JoinColumn``` in the right place:
   
   ```java
       @ManyToOne
       @JoinColumn(name = "Y_ID")
       private Y y;
   ```
   
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
    @Column(name = "GROUP_ID", nullable = false, updatable = false, insertable = false)
    private Long groupId;

    @Id
    @Column(name = "USER_ID", nullable = false, updatable = false, insertable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
```


## More Examples

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
                        select(
                                 book.string("name"), 
                                 order.longNumber("id")
                        )
                        .from(orderItem)
                        .innerJoin(orderItem.<Book> joinColumn("book"), book)
                        .innerJoin(orderItem.<Order> joinColumn("order"), order)
                        .where(book.decimalNumber("price").gt(new BigDecimal("80")))
                        .groupBy(book.longNumber("category")) 
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

- Many To Many join using ON clause:

```java

Q<Group> group = qEntity(Group.class);
Q<User> user = qEntity(User.class);
Q<UserGroup> userGroup = qEntity(UserGroup.class);

List<Group> groups = queryFactory.query()
                .select(
                        constructor(
                                Group,
                                group.longNumber("id"),
                                group.string("name")
                        ))
                .from(userGroup)
                .innerJoin(group).on(userGroup.longNumber("groupId").eq(group.longNumber("id")))
                .innerJoin(user).on(userGroup.longNumber("userId").eq(user.longNumber("id")))
                .where(user.longNumber("id").eq(2L))
                .fetch();

```

- Many To Many join using Foreign Keys:

```java

Q<Group> group = qEntity(Group.class);
Q<User> user = qEntity(User.class);
Q<UserGroup> userGroup = qEntity(UserGroup.class);

List<Group> groups = queryFactory.query()
                .select(
                        constructor(
                                Group,
                                group.longNumber("id"),
                                group.string("name")
                        ))
                .from(userGroup)
                .innerJoin(userGroup.<Group>joinColumn("group"), group)
                .innerJoin(userGroup.<User>joinColumn("user"), user)
                .where(user.longNumber("id").eq(2L))
                .fetch();

```

If you want to see more examples, please explore the integration test suite.


#### Entities used in examples:

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

@Entity
@Table(name = "USERS")
public class User<T> {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String name;
}

@Entity
@Table(name = "GROUPS")
public class Group {

    @Id
    @Column(name = "GROUP_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "USERS_GROUPS",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    private Set<User> users;

}

@Entity
@Immutable
@Table(name = "USERS_GROUPS")
public class UserGroup implements Serializable {

    @Id
    @Column(name = "GROUP_ID", nullable = false, updatable = false, insertable = false)
    private Long groupId;

    @Id
    @Column(name = "USER_ID", nullable = false, updatable = false, insertable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}

```

## Support

Although this is a project I'm working on in my spare time, I try to fix any issues as soon as I can. If you nave a feature request that could prove useful I will also consider adding it in the shortest possible time.

## BUGS

If You find any bugs, feel free to submit PR or create an issue on GitHub: https://github.com/eXsio/querydsl-entityql
