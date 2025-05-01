# J2EE Membership Management System

A comprehensive Java EE web application for managing member records with full CRUD functionality. This project demonstrates the implementation of MVC architecture using Jakarta EE technologies, JSP, Servlets, and MySQL.

## Features

- **Member Management:** Create, read, update, and delete member records.
- **Dual Interface:** Web UI and REST API endpoints.
- **MVC Architecture:** Clean separation of concerns with Model, View, Controller pattern.
- **Containerized Deployment:** Docker-based setup for both application and database.
- **Malaysian Data Integration:** Includes Malaysian postcodes and states for member address management.

## Technology Stack

- **Backend:** Jakarta EE 9, Servlets, JSP
- **Database:** MySQL 8.0
- **Build Tool:** Maven
- **Deployment:** Docker, Tomcat 11
- **Frontend:** JSP, HTML, CSS
- **API:** RESTful JSON API

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

Create a `.env` file in the project root using the `.env.example` as a template.

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

## Database Schema

The application uses the following database schema defined in `db-init/01-init.sql`:

```sql
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

## Malaysian Data Integration

- **Postcodes and States:**  
  Malaysian postcode and state data are imported from `malaysia_postcode_distinct1.json` and `states.json`.  
  Use the provided `import_postcodes_states.py` script to generate SQL insert files (`db-init/02-states_data.sql` and `db-init/03-postcodes_data.sql`).
- **Automatic Import:**  
  All SQL files in `db-init/` are executed in order on first database startup via Docker Compose.

## API Endpoints

### Member API

The application provides a RESTful API at the base path `/api/members`:

- **`GET /api/members`**: List all members.
- **`GET /api/members/{id}`**: Get a specific member by ID.
- **`POST /api/members`**: Create a new member.
- **`PUT /api/members/{id}`**: Update an existing member by ID.
- **`DELETE /api/members/{id}`**: Delete a member by ID.

#### Example API Request (Create Member)

```json
// POST /api/members
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "join_date": "2023-01-15",
  "ic_number": "900101-14-5678",
  "gender": "Male",
  "date_of_birth": "1990-01-01",
  "postcode": "01000",
  "town": "Kangar"
}
```

### Postcode API

- **`GET /api/postcodes`**: List all postcodes with their town and state code.

#### Example Response

```json
[
  { "postcode": "01000", "town": "Kangar", "state_code": "PLS" },
  { "postcode": "01007", "town": "Kangar", "state_code": "PLS" }
]
```

### State API

- **`GET /api/states`**: List all Malaysian states.

#### Example Response

```json
[
  { "state_code": "JHR", "state_name": "Johor" },
  { "state_code": "KDH", "state_name": "Kedah" }
]
```

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

## Design Patterns

- **MVC Pattern:** Separation of Model, View, and Controller.
- **DAO Pattern:** Abstraction of data persistence operations.
- **Service Layer Pattern:** Encapsulation of business logic.
- **Front Controller Pattern:** Centralized request handling via Servlets.
