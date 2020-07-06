# QueryDSL-EntityQL 

## JPA & Spring Data JDBC Native Query builder for Java, Groovy and Kotlin 

[![Build Status](https://travis-ci.org/eXsio/querydsl-entityql.svg)](https://travis-ci.org/eXsio/querydsl-entityql)
[![codecov](https://codecov.io/gh/eXsio/querydsl-entityql/branch/master/graph/badge.svg)](https://codecov.io/gh/eXsio/querydsl-entityql)
[![](https://jitpack.io/v/eXsio/querydsl-entityql.svg)](https://jitpack.io/#eXsio/querydsl-entityql)


## <a name="TLDR"></a> TL;DR 
([Contents](#Contents))

Have you ever had a situation where you had to perform a SQL operation that was not supported by JPA? 
The only solution was to create an ugly String with Native SQL, right? Well, not anymore!
With EntityQL you can create Native Queries using your own JPA Entities and a beautiful, fluent Java API. 

Don't like JPA Annotations? Don't worry, EntityQL supports also Spring Data JDBC and its metadata!

Want to use Groovy or Kotlin? EntityQL supports them and is able to generate native code for those languages.

**EntityQL can:**
- **serve as an addition to any existent JPA / Spring Data JDBC configuration or as a standalone Data Access Layer**
- **work without any additional bootstrap and warmup, which makes it a perfect choice for scalable microservices**
- **be a lightweight alternative to using JPA/Hibernate/Spring Data JDBC**
- **generate both dynamic and static Query Models, preserving the original JPA Entity field names**
- **completely replace the usage of Criteria API and/or JPQL**
- **support Native SQL features not available in JPA using fluent Java API**
- **construct QueryDSL Queries using both JPA and Spring Data JDBC Metadata systems**
- **handle all commercial and enterprise databases without any additional costs**
- **generate native Kotlin code using Maven and Gradle which makes it a perfect pick for Android development**

#### <a name="QuickExample"></a> Quick example:
([Contents](#Contents))

You can choose to use EntityQL in a more **dynamic** way:

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

Or you can go with the more traditional **static** meta-model way:

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

 **Interested? Keep on reading or jump straight to the [Examples Project](https://github.com/eXsio/querydsl-entityql-examples) for some code!**

 **Like what you see? Let me know by leaving a Star!**

## <a name="Contents"></a> Contents

1. [TL;DR](#TLDR)
    * [Quick example](#QuickExample)
2. [Motivation](#Motivation)
3. [Overview](#Overview)
4. [Supplementary Projects](#Supple)
    * [Maven Plugin](#MavenPl)
    * [Gradle Plugin](#GradlePl)
    * [Examples Project](#ExamplesProj)
5. [Usage](#Usage)    
6. [How it works](#HowItWorks)
7. [Use Cases](#UseCases)
8. [QueryDSL SQL Features](#QueryDslSqlFeatures)
9. [How does the EntityQL differ from...](#HowDiffers)
    * [Hibernate / JPA in general](#HibernateInGeneral)
    * [Hibernate / JPA Native Queries](#HibernateNative)
    * [QueryDSL-JPA in general](#QueryDslJpaInGeneral)
    * [QueryDSL-JPA JpaSqlQuery class](#queryDslJpaSql)
    * [Vanilla QueryDSL-SQL](#QueryDslSql)
    * [jOOQ](#jOOQ)
10. [Limitations and restrictions](#Limits)
11. [Performance](#Performance)
    * [Obtaining Query Model](#Obtain)
    * [Query building ](#Build)
    * [Query execution](#Execute)
12. [Thread safety](#Threads)
13. [Extending EntityQL](#Extending)
14. [More examples](#Examples)
15. [Support](#Support)
16. [Bugs](#Bugs)
    

## <a name="Motivation"></a> Motivation 
([Contents](#Contents))

Why create yet another thing to communicate with databases? As it usually happens, the reason was quite simple - none of the solutions
available on the market met my requirements and expectations, which were:
- ability to manage my Schema from Java code
- ability to create and populate test schemas in an in-memory H2 database
- ability to use Java names, not DB Objects names
- ability to create SQL queries using Fluent Java API
- ability to use advanced SQL features like Window Functions etc
- minimal performance overhead and minimal abstraction layer
- ability to work with Oracle and SQL Server Enterprise witout any additional licensing costs

QueryDSL-SQL was the closest, but it lacked the ability to create Query Models directly from JPA Entities. EntityQL is a missing piece that 
makes it possible to meet all the above requirements.

## <a name="Overview"></a> Overview 
([Contents](#Contents))

Below you can see a simple diagram that explains the role of EntityQL between JPA and QueryDSL:

![Diagram](EntityQL.png)

Red, italic, underlined is the final flow of using EntityQL in a project.
EntityQL is a tool that is able to use JPA Entity mappings and create QueryDSL-SQL meta models.
Those Models can be then used to construct Native SQL Queries based on JPA mappings, using QueryDSL fluent API.


There are two distinct ways you can utilize the power of EntityQL:
- **generate ad-hoc dynamic meta-models on the fly** using cached reflection and the ```EntityQL.qEntity``` method
- **generate static meta-model Java classes** using the **[Maven](https://github.com/eXsio/querydsl-entityql-maven-plugin)** / **[Gradle](https://github.com/eXsio/querydsl-entityql-gradle-plugin)** plugin

EntityQL works with QueryDSL-SQL, not QueryDSL-JPA. I will use the term QueryDSL in the context of QueryDSL-SQL.

## <a name="Supple"></a> Supplementary Projects
([Contents](#Contents))

There are 3 more Projects that make EntityQL a complete package:

#### <a name="MavenPl"></a> [Maven Plugin](https://github.com/eXsio/querydsl-entityql-maven-plugin) designed to easily generate Static Query Models using Maven.

#### <a name="GradlePl"></a> [Gradle Plugin](https://github.com/eXsio/querydsl-entityql-gradle-plugin) designed to easily generate Static Query Models using Gradle.

#### <a name="ExamplesProj"></a> [Examples Project](https://github.com/eXsio/querydsl-entityql-examples) created to show how to configure and use EntityQL.

## <a name="Usage"></a> Usage 
([Contents](#Contents))

There are so many different ways you can use EntityQL, that describing them all here would be inefficient and impractical.
Below you can find usage matrix with links to example projects:

| Java  | Groovy | Kotlin |
| ------------- | ------------- |------------- |
|[Maven / JPA / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/java-maven-jpa-dynamic) |[Maven / JPA / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/groovy-maven-jpa-dynamic)|[Maven / JPA / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/kotlin-maven-jpa-dynamic)|
|[Maven / JPA / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/java-maven-jpa-static) |[Maven / JPA / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/groovy-maven-jpa-static)|[Maven / JPA / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/kotlin-maven-jpa-static)|
|[Maven / Spring Data JDBC / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/java-maven-spring-data-jdbc-dynamic) |[Maven / Spring Data JDBC / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/groovy-maven-spring-data-jdbc-dynamic)|[Maven / Spring Data JDBC / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/kotlin-maven-spring-data-jdbc-dynamic)|
|[Maven / Spring Data JDBC / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/groovy-maven-spring-data-jdbc-static) |[Maven / Spring Data JDBC / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/groovy-maven-spring-data-jdbc-static)|[Maven / Spring Data JDBC / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/kotlin-maven-spring-data-jdbc-static)|
|[Gradle / JPA / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/java-gradle-jpa-dynamic) |[Gradle / JPA / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/groovy-gradle-jpa-dynamic)|[Gradle / JPA / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/kotlin-gradle-jpa-dynamic)|
|[Gradle / JPA / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/java-gradle-jpa-static) |[Gradle / JPA / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/groovy-gradle-jpa-static)|[Gradle / JPA / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/kotlin-gradle-jpa-static)|
|[Gradle / Spring Data JDBC / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/java-gradle-spring-data-jdbc-dynamic) |[Gradle / Spring Data JDBC / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/groovy-gradle-spring-data-jdbc-dynamic)|[Gradle / Spring Data JDBC / Dynamic Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/kotlin-gradle-spring-data-jdbc-dynamic)|
|[Gradle / Spring Data JDBC / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/java-gradle-spring-data-jdbc-static) |[Gradle / Spring Data JDBC / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/groovy-gradle-spring-data-jdbc-static)|[Gradle / Spring Data JDBC / Static Models](https://github.com/eXsio/querydsl-entityql-examples/tree/master/kotlin-gradle-spring-data-jdbc-static)|

Each Example Project contains a fully configured Spring Boot application that cotntains all required configuration and
some practical use cases for you to check out. Pick the one that suits your needs and let the SQL be a first party member of your project.


## <a name="HowItWorks"></a> How it works 
([Contents](#Contents))

There is a special method ```EntityQL::qEntity``` that uses (cached) Reflection to gather all DDL information required to construct 
QueryDSL meta-model and to sucessfuly perform all operations supported by QueryDSL. The scan occurs once per Entity class - 
the Annotation metadata is cached in memory for further reuse.

The dynamic meta-models are created in memory, there is no code generation during compilation or runtime. The resulting instance
of ```Q``` class contains ```Maps``` containing the mappings between the Entity's field names and the corresponding
QueryDSL-specific models that are used for constructing SQL Queries.

Once we've obtained an instance of ```Q``` class, everything down the line is just plain QueryDSL API in motion. 
Please see the examples section to see how easy it is in practice.

**If you prefer more static approach**, you can generate Static Java/Groovy/Kotlin classes with QueryDSL-SQL compatible Query Models by
using this [Maven](https://github.com/eXsio/querydsl-entityql-maven-plugin) or [Gradle](https://github.com/eXsio/querydsl-entityql-gradle-plugin) plugin.

## <a name="UseCases"></a> Use Cases 
([Contents](#Contents))

There are 3 primary use cases for EntityQL:

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
    - wants to have unbeatable persistence performance (QueryDSL is 2 to 3 times faster than Hibernate as it has minimal abstractions and works directly on JDBC level)

    EntityQL is just a translation layer between JPA mappings and QueryDSL. QueryDSL is perfectly capable to handle all DML statements.
    
3) Complementary platform for all Spring Data JDBC users

    EntityQL is able to create both Dynamic and Static Models from Spring Data JDBC Entities using Spring's own implementation 
    to gather all metadata (no reverse engineering)!    

## <a name="QueryDslSqlFeatures"></a> QueryDSL SQL Features 
([Contents](#Contents))

All of the QueryDSL-SQL features are described here: http://www.querydsl.com/static/querydsl/4.2.1/reference/html_single/#d0e1067


## <a name="HowDiffers"></a> How does the EntityQL differ from... 
([Contents](#Contents))

<a name="HibernateInGeneral"></a> 1) **Hibernate / JPA in general** - EntityQL uses Entities only  as source of DDL information necessary to construct Native SQL that is executed against JDBC Connection. 
There is no Persistence Context, no Entity Manager, no L1/L2/L3 cache, no Dirty Checking, no Cascades. That makes EntityQL offer less "magic" features, but at the same time
makes it a lot faster than Hibernate.
 
<a name="HibernateNative"></a> 2) **Hibernate / JPA Native Queries** - Hibernate offers Native SQL support only in a form of Strings containing full SQL queries. EntityQL offers full fluent QueryDSL Java API
that is way more safe and convenient to use, not to mention it is fully portable. QueryDSL has its form of Dialects that translates the query built with Java API to the SQL Dialect that matches
your Database.

<a name="QueryDslJpaInGeneral"></a> 3) **QueryDSL-JPA in general** - the JPA module is just an additional layer on top of JPA/Hibernate. All of the queries built with its API are translated into JPQL. 
Although QueryDSL-JPA API is lightyears ahead of ugly, unreadable Criteria API or error-prone JPQL Strings, it suffers from the same limited SQL operations it can support as the original JPA.

<a name="queryDslJpaSql"></a> 4) **QueryDSL-JPA JpaSqlQuery class** - the JPA module has the ability to construct Native SQL with Java API. The only limitation (at least for some) is that you need to generate both JPA
Meta Models (Q-classes created from your Entity Mappings, containing Java fields names) and SQL Meta Models (S-classes created by reverse engineering your Database Schema, containing DB Objects names). 
EntityQL doesn't require any static code generation, the Meta Models are generated on the fly and cached in memory for further reuse. If you choose to generate the static models
by exporting the ```Q``` classes, the models will have all the Java names you've created, but will allow you to perform Native SQL queries. 

<a name="QueryDslSql"></a> 5) **Vanilla QueryDSL-SQL** - it relies on generating Static Meta Model by reverse engineering your Schema. Their workflows demand that you create your Database Schema before your code.
The resulting meta-model classes will contain DB Objects names (There are Naming Strategies available but they are limited).
Most of Java developers (especially ones used to dealing with JPA) like to create their Schema in a form of JPA Entities first, and then export them to Database. 
EntityQL lets you keep your Schema management in Java Code, allows the same level of integration testing as Hibernate (you can still use Hibernate to generate your 
test in-memory H2 Database) and at the same time offers convenient, fluent Java API that constructs Native Queries and runs them directly on JDBC level, making whole thing extremely fast.
You have also an ability to choose between dynamic ad-hoc meta models and the static generated classes.

<a name="jOOQ"></a> 6) **jOOQ** - It is a huge Java framework with lots of SQL features (has a parser, supports stored procedures, DDL, procedural languages, can translate between dialects, can interpret DDL to build a meta model, 
has schema diff tool, supports multi tenancy, SQL transformation, row level type safety etc). However its focus was never (and propably will never be) on generating Meta Models from Java classes
and on working with JPA. When it comes to using jOOQ simply as a Query Builder it offers similar feature set as QueryDSL and relies on generating Static Meta Model by reverse engineering 
your Schema. It is also worth mentioning that JOOQ is not free for Enterprise Databases like Oracle or SQL Server Enterprise. EntityQL relies on an open-source stack that is free to use
for everyone and everywhere.

Please keep in mind that the above differences are sourced in my personal experiences and as such are my own subjective opinions. If you disagree with any of the comparison, please let me know.
I will gladly update them to make them more informative and objective.


## <a name="Limits"></a> Limitations and restrictions 
([Contents](#Contents))

EntityQL was created with 2 main principles in mind: simplicity and explicitness. 
All the limitations revolve around whether we have all the data needed to construct the meta-models. 

Hibernate contains a lot of magical features like auto-generation of table and column names, mapping columns to ```Map``` etc.
To work properly, EntityQL needs to work with well-formatted and completely/explicitly described Entities.

 - Entity must have a valid ```@Table``` Annotation containing the Table name and (optionally) Schema name
 - Only fields containing ```@Column```, ```@JoinColumn``` or ```@JoinColumns``` Annotations will be visible to EntityQL as DB Metadata source
 - When dealing with JPA Relations, inverse join columns will be generated. Bidirectional and unidirectional ```@OneToOne``` and ```@OneToMany```
   are fully supported for both simple and composite keys
 - In order to use Java Enums in queries, Enum classes have to be registered with QueryDSL's ```Configuration::register```
   using ```EnumType```. Alternatively you can use the provided ```EntityQlQueryFactory```. It will register all Enums from 
   the packages you want. 
 - In order to use UUIDs, you have to register ```UtilUUIDType``` with QueryDSL's ```Configuration::register```
 - In order to use Booleans you have to register ```BooleanType``` with QueryDSL's ```Configuration::register```
 - Composite Primary Keys are supported only in a form of ```Serializable @Entity``` having multiple fields annotated with ```@Id```. Embedded classes and Ids are not supported.
 - Composite Foreign Keys are supported via the ```@JoinColumns``` Annotation.
 - The only unsupported JPA Relation is ```@ManyToMany``` along with```@JoinTable```. The reason is that the auto-generated Join Table
   has no physical Entity class and there is no possibility to generate the Model without a whole lot of magic that I would rather like to avoid.
   If you want to use EntityQL with ```@ManyToMany``` mapping, you can create an ```@Immutable @Entity``` that matches the table configured in ```@JoinTable```, for example:
    
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

## <a name="Performance"></a> Performance 
([Contents](#Contents))

A lot of developers are scared anytime they see Java Reflection in use. Even though EntityQL needs to use Reflection
to scan the Entity classes, the results of the scan are cached in memory, so the actual Reflection is used only during 
the first creation of the ```Q``` model.

Having that said, there are always cases when a cutting edge performance is required (for example when dynamically scaling micro-services with Kubernetes).
In such situations we can skip the dynamic models altogether and focus on Static Meta Models.

To find out how much is the Static model faster than the Dynamic one, I've decided to implement couple of simple benchmarks 
using JMH (you can find them in the test suite):

#### <a name="Obtain"></a> Obtaining Query Model 
([Contents](#Contents))

| Method  | Score |
| ------------- | ------------- |
| EntityQL: ```QTestEntity.INSTANCE;```  | 318,653,689.654 ops/s  |
| EntityQL: ```new QTestEntity();```  | 1,715,702.909 ops/s  |
| EntityQL: ```qEntity(TestEntity.class);```  | 564,140.707 ops/s  |


Please note that the actual performance may vary depending on the complexity of the source Entity (number of fields, FKs etc).
The important thing is the difference between particular methods.


#### <a name="Build"></a> Query building 
([Contents](#Contents))

| Method  | Score |
| ------------- | ------------- |
| EntityQL: ```QTestEntity.INSTANCE;```  | 1,438,755.075 ops/s  |
| EntityQL: ```new QTestEntity();```  | 731,034.160 ops/s  |
| EntityQL: ```qEntity(TestEntity.class);```  | 392,864.171 ops/s  |


Please note that the actual performance may vary depending on the complexity of the source Entity (number of fields, FKs etc) and the complexity of the Query.
The important thing is the difference between particular methods.


#### <a name="Execute"></a> Query execution 
([Contents](#Contents))

| Method  | Score |
| ------------- | ------------- |
| JDBC: Statement  | 94,786.871 ops/s  |
| EntityQL: ```QTestEntity.INSTANCE;```  | 52,080.800 ops/s  |
| EntityQL: ```new QTestEntity();```  | 49,222.448 ops/s  |
| EntityQL: ```qEntity(TestEntity.class);```  | 45,100.517 ops/s  |
| JPA: JPQL  | 18,946.990 ops/s  |
| JPA: Criteria API | 17,933.423 ops/s  |


Please note that the actual performance may vary depending on the complexity of the source Entity (number of fields, FKs etc), 
the complexity of the Query and the amount of returned data. The important thing is the difference between particular methods.


## <a name="Threads"></a> Thread safety 
([Contents](#Contents))

Both Dynamic and Static models are Threadsafe. You can use the same instances across different Threads.

## <a name="Extending"></a> Extending EntityQL 
([Contents](#Contents))

Although EntityQL was primarily created to work with JPA Entities, there is a possibility to implement your own ```QEntityScanner```
that will analyze the Entity class and provide an instance of ```QEntityMetadata```. This opens up EntityQL to possibilities 
of connecting QueryDSL to other Persistence Frameworks, like Spring Data JDBC etc.

Once you have your ```QEntityScanner```, you can create your ```Q``` instance by calling ```EntityQL.qEntity(YourEntity.class, new YourEntityScanner())```.

## <a name="Examples"></a> More Examples 
([Contents](#Contents))

You can find fully functional and configured Spring Boot based application in the [Examples Project](https://github.com/eXsio/querydsl-entityql-examples).

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

## <a name="Support"></a> Support 
([Contents](#Contents))

Although this is a project I'm working on in my spare time, I try to fix any issues as soon as I can. If you nave a feature request that could prove useful I will also consider adding it in the shortest possible time.

## <a name="Bugs"></a> BUGS 
([Contents](#Contents))

If You find any bugs, feel free to submit PR or create an issue on GitHub: https://github.com/eXsio/querydsl-entityql
