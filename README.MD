# EcoDom

EcoDom is a **Spring Boot** application that integrates **PostgreSQL**, **OAuth2 authentication**, **WebSockets**, and various microservices to manage dogs and EcoDom+ entities.

## Prerequisites

Ensure you have the following installed on your system:

- **Java 17** or later
- **Maven** (for dependency management)
- **Docker & Docker Compose** (for running PostgreSQL)
- **Node.js** (for the frontend if required)
- **PostgreSQL Client** (for database management)

---

## Getting Started

### 1. Clone the Repository

```sh
git clone https://github.com/dutchu/spring-ecodom.git
```

### 2. Configure the Database

The application uses **PostgreSQL** as its database. You can either run PostgreSQL manually or use Docker.

#### Option 1: Run PostgreSQL using Docker Compose

```sh
docker-compose up -d
```

This starts a PostgreSQL container with the following credentials:

- **Username:** eco
- **Password:** dom
- **Database:** dev
- **Port:** 5454

#### Option 2: Run PostgreSQL Manually

If you prefer running PostgreSQL manually, update your `application.yml` file with your local database connection details.

---

### 3. Configure Spring Profiles

The application supports different profiles for development and production environments. To specify a profile, use the following command:

```sh
export SPRING_PROFILES_ACTIVE=dev  # or 'prod' for production
```

Available profiles:

- `dev` (default, for local development)

---

### 4. Configure Environment Variables

The application requires some sensitive values to be set as environment variables.

```sh
export SECRETS_JWT_KEY=your_jwt_secret_key
export OAUTH_CLIENT_REGISTRATION_GITHUB_CLIENT_ID=your_github_client_id
export OAUTH_CLIENT_REGISTRATION_GITHUB_CLIENT_SECRET=your_github_client_secret
export OAUTH_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=your_google_client_id
export OAUTH_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=your_google_client_secret
```

Alternatively, create a `.env` file and add the values.

---

### 5. Build and Run the Application

#### Using Maven

To run the application with the `dev` profile and environment variables from a `.env` file:

```sh
export $(grep -v '^#' .env | xargs)
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Running as a JAR

To run the application as a JAR with the `dev` profile:

```sh
export $(grep -v '^#' .env | xargs)
java -jar target/ecodom-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

The application will start on port **8081**.

---

### 6. Frontend Configuration

The frontend is expected to run on `http://localhost:3000`. Ensure your frontend is set up correctly to interact with the backend.

---

### 7. API Documentation

Once the application is running, you can access the API documentation:

- **Swagger UI:** `http://localhost:8081/swagger-ui.html`
- **API Endpoints:** Defined in your controllers

---

## Logging

Logging is configured using **Logback**, with logs written to the console and a file at `logs/myapp.log`. Log levels are set as follows:

- `org.springframework.security: DEBUG`
- `thymeleaf: DEBUG`
- `org.hibernate: DEBUG`

#### Disabling Logging

To disable logging, modify `application.yml` by setting the logging level to `OFF`:

```yaml
logging:
  level:
    root: OFF
```

Alternatively, run the application with the following environment variable:

```sh
export LOGGING_LEVEL_ROOT=OFF
```

---

## Deployment

For production deployment, ensure:

- **Environment variables** are securely stored.
- **Database credentials** are configured properly.
- **Frontend URL** is updated in `application.yml`.
- **Security configurations** are properly managed.

---

## What's Missing?

- **Frontend setup:** If there is a separate frontend, its setup and installation steps should be documented.
- **API details:** A list of available endpoints, request formats, and responses.
- **Admin credentials or role-based access configuration.**
- **Production deployment guide:** Instructions for deploying on a cloud provider.

---

## License

This project is licensed under the MIT License.

