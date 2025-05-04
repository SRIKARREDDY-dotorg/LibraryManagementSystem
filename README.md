# Library Management System

## Overview
A full-stack library management application built with Spring Boot backend and React/TypeScript frontend. The system provides comprehensive book management capabilities, user authentication, and automated email notifications through a microservices architecture.

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.1
- **Database**: MongoDB
- **Security**: JWT-based authentication
- **Message Queue**: RabbitMQ for asynchronous operations
- **Email Service**: Integrated email notifications
- **Cross-cutting Concerns**: Aspect-Oriented Programming (AOP)

### Frontend
- **Framework**: React 19.0.0 with TypeScript 5.7.2
- **Build Tool**: Vite 6.3.2
- **State Management**: React Context API
- **Routing**: React Router
- **HTTP Client**: Axios
- **Notifications**: React Toastify

## Features
- User authentication and authorization
- Book management (add, view, borrow, return)
- User profile management
- Email notifications for book operations
- Admin dashboard for inventory management
- Multiple book copies handling
- User borrowing limits (2 books per user)
- Real-time notifications

## Project Structure
```
├── Backend (Java)
│   ├── controllers (REST endpoints)
│   ├── services (Business logic)
│   ├── models (Data entities)
│   ├── repositories (Data access)
│   ├── config (Application configuration)
│   └── utils (Helper utilities)
│
└── Frontend (React/TypeScript)
    ├── components (UI components)
    ├── context (React context)
    ├── styles (CSS modules)
    └── types (TypeScript definitions)
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- MongoDB
- RabbitMQ

### Backend Setup
1. Clone the repository
2. Navigate to the root directory
3. Install dependencies:
   ```bash
   mvn install
   ```
4. Create a `.env` file with required environment variables
5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup
1. Navigate to the LibraryUI directory:
   ```bash
   cd LibraryUI
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```

## API Documentation

### User Endpoints
- POST `/api/auth/register` - Register new user
- POST `/api/auth/login` - User login
- GET `/api/users/profile` - Get user profile
- GET `/api/users/books` - Get user's borrowed books

### Book Endpoints
- GET `/api/books` - List all books
- POST `/api/books` - Add new book (Admin only)
- PUT `/api/books/{id}/borrow` - Borrow a book
- PUT `/api/books/{id}/return` - Return a book

### Admin Endpoints
- GET `/api/admin/inventory` - View library inventory
- GET `/api/admin/borrowed` - View all borrowed books
- POST `/api/admin/books` - Add new books
- PUT `/api/admin/books/{id}` - Update book details

## Testing
- Backend uses JUnit Jupiter and Mockito for unit testing
- Test coverage includes controllers, services, and utilities
- Run tests with:
  ```bash
  mvn test
  ```

## Development Guidelines
- Follow Test-Driven Development (TDD) practices
- Maintain consistent code formatting
- Write comprehensive unit tests
- Use TypeScript strict mode
- Follow React best practices and hooks guidelines

## Security Features
- JWT-based authentication
- Password encryption
- Role-based access control
- CORS configuration
- Protected API endpoints

## Assumptions
- MongoDB is running locally on default port
- RabbitMQ is configured for email notifications
- Admin users are pre-configured in the system
- Each book has a unique identifier
- Users can borrow up to 2 books at a time

## Future Enhancements
1. Implement structured logging
2. Add metrics collection and monitoring
3. Enhance frontend test coverage
4. Expand API documentation with Swagger/OpenAPI
5. Add book reservation system
6. Implement fine calculation for late returns

## Contributing
Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details
