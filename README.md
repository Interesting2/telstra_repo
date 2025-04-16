# Telstra Software Engineering Virtual Experience Program

This project was completed as part of the Telstra Software Engineering Virtual Experience Program on Forage. It simulates a real-world backend system for SIM card activation using Java and Spring Boot.

## 🚀 Project Overview

The system exposes RESTful APIs to:
- Activate a SIM card via a POST request to a mock microservice.
- Persist the activation result using JPA with an H2 in-memory database.
- Retrieve SIM card records by ID using a GET request.

## 🛠️ Technologies Used
- Java
- Spring Boot
- WebClient
- JPA/Hibernate
- H2 Database
- Cucumber (BDD testing)
- SonarQube (static code analysis)

## ✅ Features
- Controller layer for REST API endpoints
- DTOs for clean request/response handling
- Repository layer using Spring Data JPA
- Integration with a mock microservice for SIM activation
- Unit and BDD tests with Cucumber
- SonarQube analysis achieving A ratings across:
  - Security
  - Reliability
  - Maintainability
  - Security Hotspots
