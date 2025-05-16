# J2EE + Angular 19 Membership Management System

A comprehensive Jakarta EE and Angular 19 web application for managing member records with full CRUD functionality. This project demonstrates the implementation of MVC architecture using Jakarta EE technologies, Servlets for REST API with JSON, Angular 19, and MySQL.

---

## Features

- **Member Management:** Create, read, update, and delete member records.
- **User Authentication:** JWT-based login and registration for API access.
- **Password Reset:** Email-based password reset with verification codes via MailerSend.
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
- **Email Service:** MailerSend for password reset notifications

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
- MailerSend account (for password reset emails)

### Environment Setup

Create a `.env` file in the project root using the `.env.example` as a template.  
**Required variables:**

- `JWT_SECRET` — a secure, random string for signing JWTs.
- `JWT_EXPIRATION` — (optional) JWT expiration in milliseconds (default: 86400000 for 24h).
- `MAILERSEND_API_TOKEN` — your MailerSend API token.
- `MAILERSEND_FROM_EMAIL` — verified sender email address.
- `MAILERSEND_FROM_NAME` — sender name for emails.
- `PASSWORD_RESET_EXPIRATION` — (optional) reset code expiration in milliseconds (default: 900000 for 15min).

Example `.env`:

```env
MYSQL_DATABASE=yourdb
MYSQL_ROOT_PASSWORD=yourrootpw
MYSQL_USER=youruser
MYSQL_PASSWORD=yourpw
JWT_SECRET=your-very-long-random-secret-key-here
JWT_EXPIRATION=86400000
MAILERSEND_API_TOKEN=mlsn.your_api_token_here
MAILERSEND_FROM_EMAIL=noreply@yourdomain.com
MAILERSEND_FROM_NAME=Your App Name
PASSWORD_RESET_EXPIRATION=900000
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

CREATE TABLE IF NOT EXISTS password_resets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    verification_code VARCHAR(10) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
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

### Password Reset

- **Initiate Reset:**  
  `POST /api/auth/password-reset/initiate`

  ```json
  {
    "email": "alice@example.com"
  }
  ```

- **Verify Code:**  
  `POST /api/auth/password-reset/verify-code`

  ```json
  {
    "verificationCode": "123456"
  }
  ```

  Response:

  ```json
  {
    "message": "Verification code is valid",
    "resetToken": "<reset-token>"
  }
  ```

- **Reset Password:**  
  `POST /api/auth/password-reset/reset`
  ```json
  {
    "resetToken": "<reset-token>",
    "newPassword": "newpassword123"
  }
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

## Email Configuration

### MailerSend Setup

1. **Create MailerSend Account:**

   - Sign up at [mailersend.com](https://www.mailersend.com)
   - Verify your domain in the dashboard
   - Add required DNS records (SPF, DKIM, DMARC)

2. **Generate API Token:**

   - Go to Settings → API Tokens
   - Create token with "Email" permissions
   - Add to your `.env` file

3. **Configure Environment:**
   ```env
   MAILERSEND_API_TOKEN=mlsn.your_token_here
   MAILERSEND_FROM_EMAIL=noreply@yourdomain.com
   MAILERSEND_FROM_NAME=Your App Name
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
- **Email Service Integration:** Abstracted email sending with MailerSend.

---
