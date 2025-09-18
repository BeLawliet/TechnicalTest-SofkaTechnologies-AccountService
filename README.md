# Account Service

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)  
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)  
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)  
[![Docker](https://img.shields.io/badge/Docker-ready-blue)](https://www.docker.com/)  
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

---

## Descripción

`account-service` es un microservicio desarrollado con **Java Spring Boot** que gestiona información de **cuentas bancarias y movimientos (transacciones)**.  
Permite crear, actualizar, listar y eliminar cuentas, así como registrar y consultar movimientos asociados.  

Este servicio está **dockerizado** y preparado para ejecutarse en entornos con contenedores, lo que facilita su despliegue e integración.

---

## Funcionalidades

- **CRUD de cuentas**:
  - Crear nuevas cuentas.
  - Actualizar información de cuentas existentes.
  - Listar todas las cuentas.
  - Eliminar cuentas.
- **Gestión de transacciones**:
  - Registrar depósitos y retiros.
  - Consultar historial de transacciones por cuenta.
- **Validación de datos** con **Spring Validation** y anotaciones personalizadas (`@CheckTransaction`).
- Conversión entre entidades y DTOs mediante **ModelMapper**.
- Manejo global de excepciones con `AppControllerAdvice`.
- Persistencia en **PostgreSQL** usando **Spring Data JPA**.

---

## Tecnologías y Dependencias

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **Spring Web**
- **PostgreSQL**
- **Lombok**
- **ModelMapper**
- **Spring Boot Starter Validation**
- **Spring Boot Starter Test**
- **JaCoCo** para métricas de cobertura de tests

---

## Estructura del Proyecto

```
com.app
├─ advice
│  └─ AppControllerAdvice.java
├─ config
│  └─ AppConfig.java
├─ persistence
│  ├─ model
│  │  ├─ Account.java
│  │  ├─ Transaction.java
│  │  ├─ EAccountType.java
│  │  ├─ ETransactionType.java
│  │  └─ EStatus.java
│  └─ repository
│     ├─ IAccountRepository.java
│     └─ ITransactionRepository.java
├─ presentation
│  ├─ controller
│  │  ├─ AccountController.java
│  │  └─ TransactionController.java
│  └─ dto
│     ├─ AccountDTO.java
│     ├─ TransactionDTO.java
│     ├─ SaveTransactionDTO.java
│     ├─ UpdateAccountDTO.java
│     └─ ResponseDTO.java
├─ service
│  ├─ IAccountService.java
│  ├─ ITransactionService.java
│  └─ impl
│     ├─ AccountServiceImpl.java
│     └─ TransactionServiceImpl.java
├─ util
│  ├─ anotation
│  │  └─ CheckTransaction.java
│  └─ validation
│     └─ CheckTransactionValidator.java
└─ AccountServiceApplication.java
```

---

## Configuración

### Archivo `application.yml`

```yaml
server:
  port: ${SERVER_PORT:9090}

spring:
  application:
    name: account-service

  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: none
```

---

## Construcción y Ejecución

1. **Construcción del proyecto y creación de imagen Docker**:
   ```bash
   mvn clean install -DskipTests
   docker build -t account-service .
   ```

2. **Ejecución del contenedor**:
   ```bash
   docker run -p 9090:9090 -e SERVER_PORT=8081 -e DATABASE_URL="url" -e DATABASE_USER="user" -e DATABASE_PASSWORD="password" account-service-container
   ```

---

## Endpoints principales

### Cuentas
| Método | Endpoint                     | Descripción                        |
|--------|-------------------------------|------------------------------------|
| GET    | /api/v1/accounts             | Listar todas las cuentas           |
| POST   | /api/v1/accounts             | Crear una nueva cuenta             |
| PUT    | /api/v1/accounts/{id}        | Actualizar una cuenta existente    |
| DELETE | /api/v1/accounts/{id}        | Eliminar una cuenta                |

### Transacciones
| Método | Endpoint                              | Descripción                          |
|--------|----------------------------------------|--------------------------------------|
| GET    | /api/v1/accounts/{id}/transactions    | Listar transacciones de una cuenta   |
| POST   | /api/v1/transactions                  | Registrar una nueva transacción      |

---

## Pruebas Unitarias

- Se utilizan **Spring Boot Test** y **JUnit 5**.
- Cobertura de tests validada con **JaCoCo**.

---

## Autor

Andrés Suárez - Desarrollador Backend  
Email: elcostexd995@gmail.com  
