# QueryDSL EntityQL - Native Query builder for JPA and Spring Data JDBC

[![Build Status](https://travis-ci.org/eXsio/querydsl-entityql.svg?branch=master)](https://travis-ci.org/eXsio/querydsl-entityql)
[![codecov](https://codecov.io/gh/eXsio/querydsl-entityql/branch/master/graph/badge.svg)](https://codecov.io/gh/eXsio/querydsl-entityql)



## <a name="TLDR"></a> TL;DR 
([Contents](#Contents))

Have you ever had a situation where you had to perform a SQL operation that was not supported by JPA? 
The only solution was to create an ugly String with Native SQL, right? Well, not anymore!
With EntityQL you can create Native Queries using your own JPA Entities and a beautiful, fluent Java API. 

Don't like JPA Annotations? Don't worry, EntityQL supports also Spring Data JDBC!

**EntityQL can:**
- **serve as an addition to any existent JPA configuration or as a standalone Data Access Layer**
- **be a lightweight alternative to using JPA/Hibernate**
- **generate both dynamic and static Query Models, preserving the original JPA Entity field names**
- **completely replace the usage of Criteria API and/or JPQL**
- **support Native SQL features not available in JPA using fluent Java API**
- **construct QueryDSL Queries using both JPA and Spring Data JDBC Metadata systems**
- **handle all commercial and enterprise databases without any additional costs**

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

## <a name="Contents"></a> Contents

1. [TL;DR](#TLDR)
    * [Quick example](#QuickExample)
2. [Motivation](#Motivation)
3. [Overview](#Overview)
4. [Supplementary Projects](#Supple)
    * [Maven Plugin](#MavenPl)
    * [Examples Project](#ExamplesProj)
5. [How it works](#HowItWorks)
6. [Use Cases](#UseCases)
7. [QueryDSL SQL Features](#QueryDslSqlFeatures)
8. [How does the EntityQL differ from...](#HowDiffers)
    * [Hibernate / JPA in general](#HibernateInGeneral)
    * [Hibernate / JPA Native Queries](#HibernateNative)
    * [QueryDSL-JPA in general](#QueryDslJpaInGeneral)
    * [QueryDSL-JPA JpaSqlQuery class](#queryDslJpaSql)
    * [Vanilla QueryDSL-SQL](#QueryDslSql)
    * [jOOQ](#jOOQ)
9. [Installation](#Installation)
10. [Configuration](#Configuration)
11. [Spring Configuration](#SpringConfiguration)
12. [Spring Data JDBC Integration](#SpringDataJDBC)
13. [Static Code generation](#StaticCodeGen)
14. [Limitations and restrictions](#Limits)
15. [Performance](#Performance)
    * [Obtaining Query Model](#Obtain)
    * [Query building ](#Build)
    * [Query execution](#Execute)
16. [Thread safety](#Threads)
17. [Extending EntityQL](#Extending)
18. [More examples](#Examples)
19. [Support](#Support)
20. [Bugs](#Bugs)
    

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

EntityQL is a tool that is able to use JPA Entity mappings and create QueryDSL-SQL meta models.
Those Models can be then used to construct Native SQL Queries based on JPA mappings, using QueryDSL fluent API.

There are two distinct ways you can utilize the power of EntityQL:
- **generate ad-hoc dynamic meta-models on the fly** using cached reflection and the ```EntityQL.qEntity``` method
- **generate static meta-model Java classes** using the ```QExporter``` or **[designated Maven plugin](https://github.com/eXsio/querydsl-entityql-maven-plugin)**

EntityQL works with QueryDSL-SQL, not QueryDSL-JPA. I will use the term QueryDSL in the context of QueryDSL-SQL.

## <a name="Supple"></a> Supplementary Projects
([Contents](#Contents))

There are 2 more Projects that make EntityQL a complete package:

#### <a name="MavenPl"></a> [Maven Plugin](https://github.com/eXsio/querydsl-entityql-maven-plugin) designed to easily generate Static Query Models.

#### <a name="ExamplesProj"></a> [Examples Project](https://github.com/eXsio/querydsl-entityql-examples) created to show how to configure and use EntityQL.

## <a name="HowItWorks"></a> How it works 
([Contents](#Contents))

There is a special method ```EntityQL::qEntity``` that uses Reflection to gather all DDL information required to construct 
QueryDSL meta-model and to sucessfuly perform all operations supported by QueryDSL. The scan occurs once per Entity class - 
the Annotation metadata is cached in memory for further reuse.

The dynamic meta-models are created in memory, there is no code generation during compilation or runtime. The resulting instance
of ```Q``` class contains ```Maps``` containing the mappings between the Entity's field names and the corresponding
QueryDSL-specific models that are used for constructing SQL Queries.

Once we've obtained an instance of ```Q``` class, everything down the line is just plain QueryDSL API in motion. 
Please see the examples section to see how easy it is in practice.

**If you prefer more static approach**, you can generate Static Java classes with QueryDSL-SQL compatible Query Models by
using this [Maven Plugin](https://github.com/eXsio/querydsl-entityql-maven-plugin).

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

## <a name="Installation"></a> Installation 
([Contents](#Contents))

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
    <version>2.2.2</version>
</dependency>

<!-- QueryDSL itself -->
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-sql</artifactId>
    <version>4.2.2</version>
</dependency>

<!-- if you'd like to use JPA Annotations as source of Metadata -->
<dependency>
    <groupId>org.hibernate.javax.persistence</groupId>
    <artifactId>hibernate-jpa-2.1-api</artifactId>
    <version>1.0.2.Final</version>
</dependency>

<!-- if you'd like to use Spring Data JDBC as source of Metadata -->
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-jdbc</artifactId>
    <version>1.1.1.RELEASE</version>
</dependency>

```

## <a name="Configuration"></a> Configuration 
([Contents](#Contents))

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

## <a name="SpringConfiguration"></a> Spring Configuration 
([Contents](#Contents))

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
    return new EntityQlQueryFactory(new Configuration(sqlTemplates), dataSource)
        .registerEnumsByName("your.named.enums.package")
        .registerEnumsByOrdinal("your.ordinal.enums.package");
}

```

## <a name="SpringDataJDBC"></a> Spring Data JDBC Integration
([Contents](#Contents))

You can obtain instances of Dynamic Query Models using the following code snippet:

```java

//Use NamingStrategy of your choice
QEntityScanner scanner = new SpringDataJdbcQEntityScanner(NamingStrategy.INSTANCE);
Q<Book> book = EntityQL.qEntity(Book.class, scanner);
```

You can export the ```Q``` dynamic model using ```QExporter``` tool or [Maven Plugin](https://github.com/eXsio/querydsl-entityql-maven-plugin).

## <a name="StaticCodeGen"></a> Static Code generation 
([Contents](#Contents))

The primary method of generating Static Meta-models is to use the special [Maven Plugin](https://github.com/eXsio/querydsl-entityql-maven-plugin).

If however you need to generate the code manually, you can do so by using the ```QExporter``` utility. It uses the ```Q``` class to 
render the Java code with models compatible with QueryDSL-SQL format:

```java
String fileNamePattern = "Q%s.java"; // file/class name pattern
String packageName = "com.example.yourpackage"; //package of the generated class
String destinationPath = "/some/destination/path"; //physical location of resulting *.java file

//this will generate a Java class under "/some/destination/path/com/example/yourpackage/QYourEntity.java"
new QExporter().export(qEntity(YourEntity.class), fileNamePattern, packageName, destinationPath);

```

Generated classes are fully compatible with Java and Groovy. You can mix and match dynamic and static models:

```java

 //mix and match dynamic and static models
 QBook book = QBook.INSTANCE; 
 QOrder order = QOrder.INSTANCE;
 Q<OrderItem> orderItem = qEntity(OrderItem.class);

//use them by creating and executing a Native Query using QueryDSL API
Long count = queryFactory.select(count())
                .from(
                        select(
                                 book.name, 
                                 order.id
                        )
                        .from(orderItem)
                        .innerJoin(orderItem.<Book> joinColumn("book"), book)
                        .innerJoin(orderItem.<Order> joinColumn("order"), order)
                        .where(book.price.gt(new BigDecimal("80")))
                        .groupBy(book.category) 
                ).fetchOne();

```
 If you'd like to generate the code using the above method, you will need additional dependencies:

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

## <a name="Limits"></a> Limitations and restrictions 
([Contents](#Contents))

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
