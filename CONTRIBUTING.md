# Contributing to Clean Architecture Finance Manager

This document outlines the coding standards and design decisions for the Clean Architecture Finance Manager project.

## Code Style Guidelines

### Import Order
Imports should be organized in the following order, with no blank lines between imports of the same group:

1. Project imports (com.finance.*)
2. Jakarta imports (jakarta.persistence.*)
3. Lombok imports (lombok.*)
4. Spring imports (org.springframework.*)
5. Java imports (java.util.*)

### Lombok Usage
- Use `@Getter` at class level to generate all getters
- Use `@EqualsAndHashCode(of = "id")` for entity identity
- Use `@Setter` only on specific fields that need setters
- Remove redundant getter/setter documentation when using Lombok

### Code Organization
1. Fields (class members)
2. Constructors
3. Public methods
4. Private methods

### Documentation
- Maintain Javadoc for public APIs
- Remove redundant documentation when using Lombok
- All documentation must be in English
- Document design decisions and non-obvious implementations

## Clean Architecture Principles

### Package Structure
- Domain models in `domain.model` package
- Persistence adapters in `infrastructure.persistence`
- Clear separation between domain and infrastructure code
- Repository interfaces in domain layer

### Design Decisions
- Entities are final and not designed for extension
- Use interfaces for repositories in domain layer
- Keep domain models free from infrastructure concerns
- Use adapter pattern for persistence layer

## Build and Quality Tools

### Code Quality
- Checkstyle with Google Java Style Guide
- SpotBugs for static code analysis
- PMD for additional code quality checks
- All quality checks must pass before merging
