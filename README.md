# Account Service

Microservice coding exam solution using **Java 8**, **Spring Boot 2.6**, **H2 in-memory database**, and **Spring Data JPA**.

## Overview

This service provides two REST APIs:

1. **Create Account**
    - `POST /api/v1/account`
2. **Customer Inquiry**
    - `GET /api/v1/account/{customerNumber}`

The project was implemented based on the provided exam requirements.

## Tech Stack

- Java 8
- Spring Boot 2.6
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5
- MockMvc

## Project Structure

```text
src/main/java/com/shiela/metrobank/accountservice
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── repository
├── service
└── AccountServiceApplication.java