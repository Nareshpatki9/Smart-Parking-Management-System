# Smart Parking Management System

A full-stack web application that manages parking slots, vehicle entry and exit, billing, and parking availability.

## Technologies Used

### Backend

* Java 21
* Spring Boot
* Spring Security and JWT Authentication
* Spring Data JPA / Hibernate
* MySQL
* Maven

### Frontend

* React.js
* JavaScript
* Axios
* React Router
* HTML and CSS

## Features

* User registration and login
* JWT-based authentication
* Role-based access for users and administrators
* Automatic parking slot allocation
* Vehicle entry and exit management
* Parking slot availability tracking
* Vehicle parking history
* Billing based on vehicle type and parking duration
* Automatic slot release after vehicle exit
* Admin dashboard with parking statistics
* Exception handling and input validation

## Project Structure

```text
Smart-Parking-Management-System/
├── smart-parking-backend/
│   ├── src/
│   └── pom.xml
├── smart-parking-frontend/
│   ├── src/
│   └── package.json
├── .gitignore
└── README.md
```

## Prerequisites

Install the following before running the project:

* JDK 21
* MySQL Server
* Node.js and npm
* Maven, or use the Maven Wrapper included with the backend
* Visual Studio Code or another IDE

## Database Setup

1. Start MySQL.
2. Create the database:

```sql
CREATE DATABASE smart_parking;
```

3. Configure your local database username and password in `application-local.properties`.
4. Ensure the backend uses the correct database connection settings.

## Run the Backend

Open a terminal in `smart-parking-backend` and run:

```bash
mvn spring-boot:run
```

The backend runs at:

`http://localhost:8080`

## Run the Frontend

Open another terminal in `smart-parking-frontend` and run:

```bash
npm install
npm run dev
```

Open the local frontend URL displayed in the terminal.

## Main API Routes

| Module             | Route         |
| ------------------ | ------------- |
| Authentication     | `/auth/**`    |
| Parking slots      | `/parking/**` |
| Vehicle management | `/vehicle/**` |
| Billing            | `/billing/**` |
| Administration     | `/admin/**`   |

The exact endpoints and required authentication depend on the backend controller and security configuration.

## Security

* JWT is used for authentication.
* Database credentials and JWT secrets must remain in local configuration or environment variables.
* Do not commit `application-local.properties` or other files containing secrets.

## Future Improvements

* Online payment integration
* Email and SMS notifications
* Parking reservation system
* Deployment to a cloud platform

## Author

Naresh Patki

B.Sc. Computer Science | Java and Full Stack Development