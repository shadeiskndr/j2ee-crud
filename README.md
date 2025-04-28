# J2EE Membership Management System

A comprehensive Java EE web application for managing member records with full CRUD functionality. This project demonstrates the implementation of MVC architecture using Jakarta EE technologies, JSP, Servlets, and MySQL.

## Features

- **Member Management:** Create, read, update, and delete member records.
- **Dual Interface:** Web UI and REST API endpoints.
- **MVC Architecture:** Clean separation of concerns with Model, View, Controller pattern.
- **Containerized Deployment:** Docker-based setup for both application and database.

## Technology Stack

- **Backend:** Jakarta EE 9, Servlets, JSP
- **Database:** MySQL 8.0
- **Build Tool:** Maven
- **Deployment:** Docker, Tomcat 11
- **Frontend:** JSP, HTML, CSS
- **API:** RESTful JSON API

## Project Structure

```text
j2ee-crud
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           ├── controller
│       │           │   ├── MemberApiServlet.java
│       │           │   └── MemberServlet.java
│       │           ├── dao
│       │           │   ├── MemberDAO.java
│       │           │   └── MemberDAOImpl.java
│       │           ├── model
│       │           │   └── Member.java
│       │           ├── service
│       │           │   ├── MemberService.java
│       │           │   └── MemberServiceImpl.java
│       │           └── util
│       │               └── DBUtil.java
│       └── webapp
│           ├── index.jsp
│           └── WEB-INF
│               ├── views
│               │   ├── add-member.jsp
│               │   ├── edit-member.jsp
│               │   └── list-members.jsp
│               └── web.xml
├── docker-compose.yml
├── Dockerfile
├── init.sql
└── pom.xml
```

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

## Getting Started

### Prerequisites

- Docker and Docker Compose
- JDK 21 (for development)
- Maven (for development)

### Environment Setup

Create a `.env` file in the project root with the following variables:

```dotenv:.env
MYSQL_DATABASE=memberdb
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_USER=memberuser
MYSQL_PASSWORD=memberpass
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
4.  Access the application at `http://localhost` (or the appropriate port if configured differently, typically 8080 for Tomcat).

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

## API Endpoints

The application provides a RESTful API at the base path `/api/members`:

- **`GET /api/members`**: List all members.
- **`GET /api/members/{id}`**: Get a specific member by ID.
- **`POST /api/members`**: Create a new member.
- **`PUT /api/members/{id}`**: Update an existing member by ID.
- **`DELETE /api/members/{id}`**: Delete a member by ID.

### Example API Request (Create Member)

```json
// POST /api/members
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "join_date": "2023-01-15"
}
```

## Database Schema

The application uses the following database schema defined in `init.sql`:

```sql:init.sql
CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    join_date DATE NOT NULL
);
```

## Design Patterns

- **MVC Pattern:** Separation of Model, View, and Controller.
- **DAO Pattern:** Abstraction of data persistence operations.
- **Service Layer Pattern:** Encapsulation of business logic.
- **Front Controller Pattern:** Centralized request handling via Servlets.
