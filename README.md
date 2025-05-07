# J2EE Membership Management System

A comprehensive Java EE web application for managing member records with full CRUD functionality. This project demonstrates the implementation of MVC architecture using Jakarta EE technologies, JSP, Servlets, and MySQL.

---

## Features

- **Member Management:** Create, read, update, and delete member records.
- **User Authentication:** JWT-based login and registration for API access.
- **Password Security:** User passwords are stored as Argon2 hashes.
- **Dual Interface:** Web UI and REST API endpoints.
- **MVC Architecture:** Clean separation of concerns with Model, View, Controller pattern.
- **Containerized Deployment:** Docker-based setup for both application and database.
- **Malaysian Data Integration:** Includes Malaysian postcodes and states for member address management.

---

## Technology Stack

- **Backend:** Jakarta EE 9, Servlets, JSP
- **Database:** MySQL 8.0
- **Build Tool:** Maven
- **Deployment:** Docker, Tomcat 11
- **Frontend:** JSP, HTML, CSS, Angular (served via Nginx)
- **API:** RESTful JSON API
- **Authentication:** JWT (via [jjwt](https://github.com/jwtk/jjwt)), Argon2 password hashing (via [argon2-jvm](https://github.com/phxql/argon2-jvm))

---

## Architecture

The application follows a layered architecture:

- **Presentation Layer:**
  - `JSP` views for the web interface.
  - `Servlets` for handling HTTP requests.
  - `JSON` responses for the API.
- **Business Logic Layer:**
  - Service classes implementing business rules.
  - Data validation and processing.
- **Data Access Layer:**
  - `DAO` (Data Access Object) pattern for database operations.
  - `JDBC` for database connectivity.

---

## Getting Started

### Prerequisites

- Docker and Docker Compose
- JDK 21 (for development)
- Maven (for development)

### Environment Setup

Create a `.env` file in the project root using the `.env.example` as a template.  
**New required variables:**

- `JWT_SECRET` — a secure, random string for signing JWTs.
- `JWT_EXPIRATION` — (optional) JWT expiration in milliseconds (default: 86400000 for 24h).

Example `.env`:

```env
MYSQL_DATABASE=yourdb
MYSQL_ROOT_PASSWORD=yourrootpw
MYSQL_USER=youruser
MYSQL_PASSWORD=yourpw
JWT_SECRET=your-very-long-random-secret-key-here
JWT_EXPIRATION=86400000
```

### Running the Application

1.  Clone the repository:
    ```bash
    git clone <repository-url>
    ```
2.  Navigate to the project directory:
    ```bash
    cd j2ee-crud
    ```
3.  Run the application using Docker Compose:
    ```bash
    docker compose up
    ```
4.  Access the application at `http://localhost:8080`.

---

## Database Schema

The application uses the following database schema defined in `db-init/01-init.sql`:

```sql
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    join_date DATE NOT NULL,
    ic_number VARCHAR(20) NOT NULL UNIQUE,
    gender VARCHAR(10) NOT NULL,
    date_of_birth DATE NOT NULL,
    postcode VARCHAR(10) NOT NULL,
    town VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS states (
    state_code VARCHAR(5) PRIMARY KEY,
    state_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS postcodes (
    postcode VARCHAR(10) PRIMARY KEY,
    town VARCHAR(100) NOT NULL,
    state_code VARCHAR(5) NOT NULL,
    FOREIGN KEY (state_code) REFERENCES states(state_code)
);
```

---

## User Authentication & Authorization

### Registration & Login

- **Register:**  
  `POST /api/auth/register`
  ```json
  {
    "username": "alice",
    "email": "alice@example.com",
    "password": "secret"
  }
  ```
- **Login:**  
  `POST /api/auth/login`
  ```json
  {
    "username": "alice",
    "password": "secret"
  }
  ```
  Response:
  ```json
  { "token": "<JWT token>" }
  ```

### JWT-Protected Endpoints

All `/api/members`, `/api/postcodes`, and `/api/states` endpoints require a valid JWT in the `Authorization` header:

```
Authorization: Bearer <token>
```

If the token is missing or invalid, the API responds with HTTP 401 Unauthorized.

---

## API Endpoints

### Member API

The application provides a RESTful API at the base path `/api/members`:

#### **GET /api/members** — List Members

Supports pagination, search, and sorting.

**Query Parameters:**

- `pageIndex` (integer, zero-based): Page number (default: 0)
- `pageSize` (integer): Number of records per page (default: 10)
- `start` (integer): Offset (alternative to pageIndex, zero-based)
- `search` (string): Global search on `name`, `email`, `ic_number`, `gender`, `postcode`, `town`
- `sortField` (string): Field to sort by (default: `join_date`)
- `sortOrder` (string): `asc` or `desc` (default: `desc`)

**Examples:**

- First page (default):
  ```
  GET /api/members
  ```
- Second page, 20 per page:
  ```
  GET /api/members?pageIndex=1&pageSize=20
  ```
- Offset-based:
  ```
  GET /api/members?start=50&pageSize=10
  ```
- Search:
  ```
  GET /api/members?search=alice
  ```
- Sort by name ascending:
  ```
  GET /api/members?sortField=name&sortOrder=asc
  ```
- Combined:
  ```
  GET /api/members?pageIndex=0&pageSize=10&search=selangor&sortField=email&sortOrder=desc
  ```

**Response:**

```json
{
  "members": [
    {
      "id": 1,
      "name": "Alice",
      "email": "alice@example.com",
      "join_date": "2024-05-01",
      "ic_number": "900101-14-5678",
      "gender": "Female",
      "date_of_birth": "1990-01-01",
      "postcode": "01000",
      "town": "Kangar"
    }
    // ...
  ],
  "totalCount": 95,
  "offset": 20,
  "pageSize": 10,
  "search": "alice",
  "sortField": "name",
  "sortOrder": "asc"
}
```

#### **GET /api/members/{id}** — Get a specific member by ID

#### **POST /api/members** — Create a new member

#### **PUT /api/members/{id}** — Update an existing member by ID

#### **DELETE /api/members/{id}** — Delete a member by ID

---

### Postcode API

- **`GET /api/postcodes`**: List all postcodes with their town and state code.

### State API

- **`GET /api/states`**: List all Malaysian states.

---

## Malaysian Data Integration

- **Automatic Import:**  
  All SQL files in `db-init/` are executed in order on first database startup via Docker Compose.

---

## Automated API Testing

A sample shell script using `curl` is provided to test registration, login, and protected endpoints:

```bash
#!/bin/bash

API_URL="http://localhost:8080/api"
USERNAME="apitestuser"
EMAIL="apitestuser@example.com"
PASSWORD="SuperSecret123"

echo "=== Registering user ==="
curl -s -X POST "$API_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}"
echo -e "\n"

echo "=== Logging in ==="
LOGIN_RESPONSE=$(curl -s -X POST "$API_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}")

echo "$LOGIN_RESPONSE"
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -oP '(?<=\"token\":\")[^\"]+')

if [ -z "$TOKEN" ]; then
  echo "Login failed or token not found."
  exit 1
fi

echo -e "\n=== Accessing protected /api/members endpoint ==="
curl -s -X GET "$API_URL/members" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
echo -e "\n"

echo "=== Testing unauthorized access (should fail) ==="
curl -s -X GET "$API_URL/members"
echo -e "\n"
```

---

## Development Workflow

1.  Make changes to the source code in the `src` directory.
2.  Build the application using Maven to create the `.war` file:
    ```bash
    mvn clean package
    ```
3.  Rebuild and restart the Docker containers to deploy the changes:
    ```bash
    docker compose down
    ```
    ```bash
    docker compose up --build
    ```

---

## Design Patterns

- **MVC Pattern:** Separation of Model, View, and Controller.
- **DAO Pattern:** Abstraction of data persistence operations.
- **Service Layer Pattern:** Encapsulation of business logic.
- **Front Controller Pattern:** Centralized request handling via Servlets.
- **JWT Authentication:** Stateless, secure API authentication.
- **Password Hashing:** Secure storage of user passwords using Argon2.

---
