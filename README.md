# QueryDSL EntityQL - Native Query builder for JPA

[![Build Status](https://travis-ci.org/eXsio/querydsl-entityql.svg?branch=master)](https://travis-ci.org/eXsio/querydsl-entityql)
[![codecov](https://codecov.io/gh/eXsio/querydsl-entityql/branch/master/graph/badge.svg)](https://codecov.io/gh/eXsio/querydsl-entityql)



## TL;DR

Have you ever had a situation where you had to perform a SQL operation that was not supported by JPA? 
The only solution was to create an ugly String with Native SQL, right? Well, not anymore!
With EntityQL you can create Native Queries using your own JPA Entities and a beautiful, fluent Java API. 

#### Quick example:

You can choose to use EntityQL in a more dynamic way:

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

Or you can go with the more traditional static meta-model way:

```java

 //use static QueryDSL Models pre-generated from your Entity classes
 QBook book = QBook.INSTANCE; 
 QOrder order = QOrder.INSTANCE;
 QOrderItem orderItem = QOrderItem.INSTANCE;

//use them by creating and executing a Native Query using QueryDSL API
Long count = queryFactory.select(count())
                .from(
                        select(
                                 book.name, 
                                 order.id
                        )
                        .from(orderItem)
                        .innerJoin(orderItem.book, book)
                        .innerJoin(orderItem.order, order)
                        .where(book.price.gt(new BigDecimal("80")))
                        .groupBy(book.category) 
                ).fetchOne();

```

Interested? Keep on reading! 

## Overview

EntityQL is a tool that is able to use JPA Entity mappings and create QueryDSL-SQL meta models.
Those Models can be then used to construct Native SQL Queries based on JPA mappings, using QueryDSL fluent API.

There are two distinct ways you can utilize the power of EntityQL:
- generate ad-hoc dynamic meta-models on the fly using cached reflection and the ```EntityQL.qEntity``` method
- generate static meta-model Java classes using the ```QExporter``` or (in the future) designated Maven plugin

EntityQL works with QueryDSL-SQL, not QueryDSL-JPA. I will use the term QueryDSL in the context of QueryDSL-SQL.

## How it works

There is a special method ```EntityQL::qEntity``` that uses Reflection to gather all DDL information required to construct 
QueryDSL meta-model and to sucessfuly perform all operations supported by QueryDSL. The scan occurs once per Entity class - 
the Annotation metadata is cached in memory for further reuse.

The dynamic meta-models are created in memory, there is no code generation during compilation or runtime. The resulting instance
of ```Q``` class contains ```Maps``` containing the mappings between the Entity's field names and the corresponding
QueryDSL-specific models that are used for constructing SQL Queries.

Once we've obtained an instance of ```Q``` class, everything down the line is just plain QueryDSL API in motion. 
Please see the examples section to see how easy it is in practice.

**If you prefere more static approach**, you can export your ```Q``` classes to static Java code using the ```QExporter``` utility.
For now you have to use it manually but there are plans to create Maven plugin that will automate the code generation process.

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
    
    None of the above solutions are convenient or safe. EntityQL provides a way to circumvent those kinds of issues in a 
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
Meta Models (Q-classes created from your Entity Mappings, containing Java fields names) and SQL Meta Models (S-classes created by reverse engineering your Database Schema, containing DB Objects names). 
EntityQL doesn't require any static code generation, the Meta Models are generated on the fly and cached in memory for further reuse. If you choose to generate the static models
by exporting the ```Q``` classes, the models will have all the Java names you've created, but will allow you to perform Native SQL queries. 

5) **Vanilla QueryDSL-SQL and/or JOOQ** - both of the frameworks are offering similar feature sets and both rely on generating Static Meta Model by reverse engineering 
your Schema. Their workflows demand that you create your Database Schema before your code. In both cases the resulting meta-model classes will contain DB Objects names (There are Naming Strategies available but they are limited).
Most of Java developers (especially ones used to dealing with JPA) like to create their Schema in a form of JPA Entities first, and then export them to Database. 
EntityQL lets you keep your Schema management in Java Code, allows the same level of integration testing as Hibernate (you can still use Hibernate to generate your 
test in-memory H2 Database) and at the same time offers convenient, fluent Java API that constructs Native Queries and runs them directly on JDBC level, making whole thing extremely fast.
You have also an ability to choose between dynamic ad-hoc meta models and the static generated classes.

It is also worth mentioning that JOOQ is not free for Enterprise Databases like Oracle or SQL Server Enterprise. EntityQL relies on an open-source stack that is free to use
for everyone and everywhere.

## Installation

In the most basic form you just need EntityQL, JPA API and QueryDSL-SQL:

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
    <version>2.0.5</version>
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

## Static Code generation

For now it is only possible to generate the code using the ```QExporter``` utility. It uses the ```Q``` class to 
render the Java code with models compatible with QueryDSL-SQL format:

```java
String fileNamePattern = "Q%s.java"; // file/class name pattern
String packageName = "com.example.yourpackage"; //package of the generated class
String destinationPath = "/some/destination/path"; //physical location of resulting *.java file

//this will generate a Java class under "/some/destination/path/QYourEntity.java"
new QExporter().export(qEntity(YourEntity.class), fileNamePattern, packageName, destinationPath);

```

There will be a Maven plugin released in the near future to ease the process of code generation. If you'd like to 
generate the code using the above method, you will need additional dependencies:

```xml

    <dependency>
        <groupId>org.jtwig</groupId>
        <artifactId>jtwig-core</artifactId>
        <version>5.87.0.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.6</version>
    </dependency>
    <dependency>
        <groupId>com.google.googlejavaformat</groupId>
        <artifactId>google-java-format</artifactId>
        <version>1.7</version>
    </dependency>
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>28.2-jre</version>
    </dependency>

```

## Limitations and restrictions

EntityQL was created with 2 main principles in mind: simplicity and explicitness. 
All the limitations revolve around whether we have all the data needed to construct the meta-models. 

Hibernate contains a lot of magical features like auto-generation of table and column names, mapping columns to ```Map``` etc.
To work properly, EntityQL needs to work with well-formatted and completely/explicitly described Entities.

 - Entity must have a valid ```@Table``` Annotation containing the Table name and (optionally) Schema name
 - Only fields containing ```@Column```, ```@JoinColumn``` or ```@JoinColumns``` Annotations will be visible to EntityQL
 - When dealing with bidirectional mappings, only the sides that actually contain columns (```@JoinColumn```) will be supported,
   other sides will be ignored (```@OneToMany``` and the reversed ```@OneToOne```)
 - In order to use Java Enums in queries, Enum classes have to be registered with QueryDSL's ```Configuration::register```
   using ```EnumType```. Alternatively you can use the provided ```EntityQlQueryFactory```. It will register all Enums from 
   the packages you want. 
 - In order to use UUIDs, you have to register ```UtilUUIDType``` with QueryDSL's ```Configuration::register```
 - In order to use Booleans you have to register ```BooleanType``` with QueryDSL's ```Configuration::register```
 - Composite Primary Keys are supported only in a form of ```Serializable @Entity``` having multiple fields annotated with ```@Id```. Embedded classes and Ids are not supported.
 - Composite Foreign Keys are supported via the ```@JoinColumns``` Annotation.
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

## Performance

A lot of developers are scared anytime they see Java Reflection in use. Even though EntityQL needs to use Reflection
to scan the Entity classes, the results of the scan are cached in memory, so the actual Reflection is used only during 
the first creation of the ```Q``` model.

Having that said, there are always cases when a cutting edge performance is required (for example when dynamically scaling micro-services with Kubernetes).
In such situations we can skip the dynamic models altogether and focus on Static Meta Models.

To find out how much is the Static model faster than the Dynamic one, I've decided to implement couple of simple benchmarks 
using JMH (you can find them in the test suite). The results of the Throughput mode are:

```java

qEntity(TestEntity.class); //creation of dynamic model - 160,735 ops/s
new QTestEntity(); //instantiation of static model - 1,066,797 ops/s
QTestEntity.INSTANCE; //using pre-instantiated static model - 318,653,689 ops/s

```

The above results clearly show that the absolute top performance is possible only when using static models. However
the dynamic model's performance is also very good for most use cases.


## More Examples

All of the below examples use the dynamic ad-hoc models. All of them can be used with static meta-models as well. 
When using the static models, the String method parameters become the Fields themselves (just like in a Quick Example in the beginning).

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
                                Group.class,
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
                                Group.class,
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
