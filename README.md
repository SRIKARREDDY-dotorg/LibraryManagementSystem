# Library Management System

## Overview
This project is a simple Library Management System implemented in Java using Test-Driven Development (TDD). It allows users to view books in the library.

## Features
- View books in the library
- Add books to the library
- Each book has a unique ID
- Borrow book from the library per user limit(2)

## Architectural Decisions
- Used OOP principles to model the `Book`, `Library`, `User` classes.
- `Library` maintains a list of books and exposes a method to view them.
- Used `unmodifiableList` to ensure immutability when retrieving book data.
- `Library` put, get and removes the book in constant time using map to make library scalable.
- `User` class can view the books, and borrow books within limit.
- Followed TDD by writing unit tests before implementation.
- Each book has a unique identifier (`UUID`) for tracking.

## Assumptions
- The library starts empty.
- Books have a title, an author, and a unique ID.

## Class Diagram
![img.png](img.png)

### Demo run

```
/Library/Java/JavaVirtualMachines/amazon-corretto-17.jdk/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=54360:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/pochasri/temp/LibraryManagement/target/classes LibraryDemo
User{name='John Doe', email='john.doe@example.com', id='USRbaf0a733'}

Library is empty

Books in the library:
Book{id='BKf0179cfa', title='To Kill a Mockingbird', author='Harper Lee'}
Book{id='BK61d93d78', title='1984', author='George Orwell'}
Book{id='BK8aea4e6f', title='The Great Gatsby', author='F. Scott Fitzgerald'}

The Great Gatsby Book borrowed successfully.
To Kill a Mockingbird Book borrowed successfully.

You have reached the borrowing limit.

Borrowed books
Book{id='BK8aea4e6f', title='The Great Gatsby', author='F. Scott Fitzgerald'}
Book{id='BKf0179cfa', title='To Kill a Mockingbird', author='Harper Lee'}

Process finished with exit code 0
```