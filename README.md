# Library Management System

## Overview
This project is a simple Library Management System implemented in Java using Test-Driven Development (TDD). It allows users to view books in the library.

## Features
- View books in the library
- Add books to the library
- Each book has a unique ID
- Borrow book from the library per user limit(2)
- Multiple copies of books are handled

## Architectural Decisions
- Used OOP principles to model the `Book`, `Library`, `User` classes.
- `Library` maintains a list of books and exposes a method to view them.
- Used `unmodifiableList` to ensure immutability when retrieving book data.
- `Library` put, get and removes the book in constant time using map to make library scalable.
- `User` class can view the books, and borrow books within limit.
- `Admin` class extends the User having all features of User with additional permissions
- Followed TDD by writing unit tests before implementation.
- Each book has a unique identifier (`UUID`) for tracking.

## Assumptions
- The library starts empty.
- Books have a title, an author, and a unique ID.
- Admin can add books, check inventory and borrowed books

## Class Diagram
![img.png](img.png)

### Demo run

```
/Library/Java/JavaVirtualMachines/amazon-corretto-17.jdk/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=60774:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/pochasri/temp/LibraryManagement/target/classes:/Users/pochasri/.m2/repository/org/springframework/boot/spring-boot-starter-web/3.2.1/spring-boot-starter-web-3.2.1.jar:/Users/pochasri/.m2/repository/org/springframework/boot/spring-boot-starter/3.2.1/spring-boot-starter-3.2.1.jar:/Users/pochasri/.m2/repository/org/springframework/boot/spring-boot-starter-logging/3.2.1/spring-boot-starter-logging-3.2.1.jar:/Users/pochasri/.m2/repository/ch/qos/logback/logback-classic/1.4.14/logback-classic-1.4.14.jar:/Users/pochasri/.m2/repository/ch/qos/logback/logback-core/1.4.14/logback-core-1.4.14.jar:/Users/pochasri/.m2/repository/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar:/Users/pochasri/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.21.1/log4j-to-slf4j-2.21.1.jar:/Users/pochasri/.m2/repository/org/apache/logging/log4j/log4j-api/2.21.1/log4j-api-2.21.1.jar:/Users/pochasri/.m2/repository/org/slf4j/jul-to-slf4j/2.0.9/jul-to-slf4j-2.0.9.jar:/Users/pochasri/.m2/repository/jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar:/Users/pochasri/.m2/repository/org/springframework/spring-core/6.1.2/spring-core-6.1.2.jar:/Users/pochasri/.m2/repository/org/springframework/spring-jcl/6.1.2/spring-jcl-6.1.2.jar:/Users/pochasri/.m2/repository/org/yaml/snakeyaml/2.2/snakeyaml-2.2.jar:/Users/pochasri/.m2/repository/org/springframework/boot/spring-boot-starter-json/3.2.1/spring-boot-starter-json-3.2.1.jar:/Users/pochasri/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.15.3/jackson-databind-2.15.3.jar:/Users/pochasri/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.15.3/jackson-annotations-2.15.3.jar:/Users/pochasri/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.15.3/jackson-core-2.15.3.jar:/Users/pochasri/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jdk8/2.15.3/jackson-datatype-jdk8-2.15.3.jar:/Users/pochasri/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.15.3/jackson-datatype-jsr310-2.15.3.jar:/Users/pochasri/.m2/repository/com/fasterxml/jackson/module/jackson-module-parameter-names/2.15.3/jackson-module-parameter-names-2.15.3.jar:/Users/pochasri/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat/3.2.1/spring-boot-starter-tomcat-3.2.1.jar:/Users/pochasri/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/10.1.17/tomcat-embed-core-10.1.17.jar:/Users/pochasri/.m2/repository/org/apache/tomcat/embed/tomcat-embed-el/10.1.17/tomcat-embed-el-10.1.17.jar:/Users/pochasri/.m2/repository/org/apache/tomcat/embed/tomcat-embed-websocket/10.1.17/tomcat-embed-websocket-10.1.17.jar:/Users/pochasri/.m2/repository/org/springframework/spring-web/6.1.2/spring-web-6.1.2.jar:/Users/pochasri/.m2/repository/org/springframework/spring-beans/6.1.2/spring-beans-6.1.2.jar:/Users/pochasri/.m2/repository/io/micrometer/micrometer-observation/1.12.1/micrometer-observation-1.12.1.jar:/Users/pochasri/.m2/repository/io/micrometer/micrometer-commons/1.12.1/micrometer-commons-1.12.1.jar:/Users/pochasri/.m2/repository/org/springframework/spring-webmvc/6.1.2/spring-webmvc-6.1.2.jar:/Users/pochasri/.m2/repository/org/springframework/spring-aop/6.1.2/spring-aop-6.1.2.jar:/Users/pochasri/.m2/repository/org/springframework/spring-context/6.1.2/spring-context-6.1.2.jar:/Users/pochasri/.m2/repository/org/springframework/spring-expression/6.1.2/spring-expression-6.1.2.jar:/Users/pochasri/.m2/repository/org/springframework/boot/spring-boot-devtools/3.2.1/spring-boot-devtools-3.2.1.jar:/Users/pochasri/.m2/repository/org/springframework/boot/spring-boot/3.2.1/spring-boot-3.2.1.jar:/Users/pochasri/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/3.2.1/spring-boot-autoconfigure-3.2.1.jar com.srikar.library.LibraryDemo
User{name='John Doe', email='john.doe@example.com', id='USRfdb34ac3'}

Library is empty

Book added: The Great Gatsby
Book added: To Kill a Mockingbird
Book added: 1984
Books in the library:
Book{id='BK8beecf12', title='To Kill a Mockingbird', author='Harper Lee', stock=1}
Book{id='BK03361cd1', title='1984', author='George Orwell', stock=1}
Book{id='BK0e20d899', title='The Great Gatsby', author='F. Scott Fitzgerald', stock=2}

The Great Gatsby Book borrowed successfully.
To Kill a Mockingbird Book borrowed successfully.
To Kill a Mockingbird Book is out of stock.

You have reached the borrowing limit.

Borrowed books
Book{id='BK0e20d899', title='The Great Gatsby', author='F. Scott Fitzgerald', stock=1}
Book{id='BK8beecf12', title='To Kill a Mockingbird', author='Harper Lee', stock=0}

Library Inventory:
Book{id='BK03361cd1', title='1984', author='George Orwell', stock=1}
Book{id='BK0e20d899', title='The Great Gatsby', author='F. Scott Fitzgerald', stock=1}

Users who have borrowed books:
John Doe has borrowed:
Book{id='BK0e20d899', title='The Great Gatsby', author='F. Scott Fitzgerald', stock=1}
Book{id='BK8beecf12', title='To Kill a Mockingbird', author='Harper Lee', stock=0}

Process finished with exit code 0
```