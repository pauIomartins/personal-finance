# Personal Finance Manager

A robust web application for personal finance management, built with Clean Architecture and modern technologies. This project is being developed with the assistance of Windsurf IDE and its AI pair programming capabilities.

## Development Tools

- **Windsurf IDE**: Next-generation IDE with AI-powered development
- **Cascade AI**: Advanced AI pair programming assistant
- **Clean Architecture**: For maintainable and scalable code structure

## Technologies

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- H2 Database
- Maven

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.9+
- Git

## Installation

1. Clone the repository:
```bash
git clone https://github.com/pauIomartins/personal-finance.git
cd personal-finance
```

2. Run the application:
```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`

## Architecture

The project follows Clean Architecture principles, organized in layers:

### Project Structure

```
src/main/java/com/finance/manager/cleanarch/
├── application/
│   └── usecase/          # Application use cases
├── domain/
│   ├── model/            # Domain entities
│   └── repository/       # Repository interfaces
├── infrastructure/
│   ├── config/           # Configurations
│   ├── persistence/      # Persistence implementation
│   └── security/         # Security configurations
└── interfaces/
    ├── dto/              # Data Transfer Objects
    └── web/              # REST Controllers
```

### Application Flow

1. Requests are received by controllers in the `interfaces` layer
2. Controllers convert DTOs to domain models
3. Use cases in the `application` layer process business logic
4. The `infrastructure` layer handles technical details like persistence

## Features

- Financial transaction management
- Financial dashboard
- User authentication and authorization
- Income and expense categorization

## Security

The application uses Spring Security for:
- User authentication
- Endpoint protection
- Session management

## Testing

Run tests using:
```bash
./mvnw test
```

## Project History

This project has evolved significantly over time:

### Python Version (Initial)
The project started as a simpler application built with:
- Python Flask framework
- SQLite database
- Basic CRUD operations
- Simple authentication system

### Java Migration
The decision to migrate to Java was made to create a more robust, enterprise-ready application:
- Rewrote the entire application in Java
- Adopted Clean Architecture principles
- Implemented Spring Boot 3.x
- Enhanced security with Spring Security
- Improved database handling with H2 and JPA
- Structured the application in well-defined layers

### Current Development
The project now leverages modern development tools:
- Development in Windsurf IDE with AI assistance
- Pair programming with Cascade AI
- Dependency management with Maven
- Version control with Git/GitHub

While maintaining the core functionality, the migration to Java has significantly improved the application's architecture, maintainability, and scalability.

## License

This project is under the MIT license. See the [LICENSE](LICENSE) file for details.

## Author

[pauIomartins](https://github.com/pauIomartins)

## Acknowledgments

This project is being developed with the assistance of:
- Windsurf IDE - The world's first agentic IDE
- Cascade AI - A powerful AI pair programming assistant by Codeium

---
 with  by [pauIomartins](https://github.com/pauIomartins)