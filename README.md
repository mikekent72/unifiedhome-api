# UnifiedHome API

A Spring Boot REST API for managing smart-home devices, rooms and schedules.

UnifiedHome simulates the backend of a small smart-home system. Users can create rooms, manage devices within those rooms, control device state and create simple schedules for automated actions.

---

## Features

- Create, read, update and delete rooms
- Create, read, update and delete smart-home devices
- Create, read, update and delete device schedules
- Assign devices to rooms
- Change device enabled/disabled state
- Validate incoming API requests
- Handle common API errors with appropriate HTTP status codes
- Persist data using PostgreSQL
- RESTful JSON API
- Automated service-layer tests
- Interactive OpenAPI/Swagger API documentation

---

## Technologies

- Java
- Spring Boot
- Maven
- PostgreSQL
- Spring Data JPA
- Hibernate
- REST
- JSON
- JUnit 5
- Mockito
- OpenAPI / Swagger UI
- Git / GitHub

---

## Architecture

The application follows a layered architecture:

```text
Client
  ↓
REST Controller
  ↓
Service Layer
  ↓
Repository Layer
  ↓
PostgreSQL
```

### Controller

Handles HTTP requests and responses.

The controllers are responsible for exposing REST endpoints and passing requests to the appropriate service.

### Service

Contains the application's business logic.

For example, when creating a device, the service checks that the specified room exists before saving the device.

### Repository

Provides database access through Spring Data JPA.

The repositories keep database access separate from the application's business logic.

### DTO

Data Transfer Objects are used for API requests and responses rather than exposing JPA entities directly.

This keeps the API model separate from the database model.

---

## Database

The application uses three main entities: Room, Device and Schedule.

### Room

Represents a physical room in the home.

Examples:

- Living Room
- Bedroom
- Kitchen
- Garage

### Device

Represents a smart device belonging to a room.

Examples:

- Light
- Plug
- Fan
- Speaker

### Schedule

Represents a stored automation instruction for a device.

Examples:

- Turn the living room light on at 18:00.
- Turn the bedroom fan off at 21:00.

---

## REST API

### Rooms

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/rooms` | Get all rooms |
| GET | `/api/rooms/{id}` | Get a room |
| POST | `/api/rooms` | Create a room |
| PUT | `/api/rooms/{id}` | Update a room |
| DELETE | `/api/rooms/{id}` | Delete a room |

### Devices

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/devices` | Get all devices |
| GET | `/api/devices/{id}` | Get a device |
| POST | `/api/devices` | Create a device |
| PUT | `/api/devices/{id}` | Update a device |
| DELETE | `/api/devices/{id}` | Delete a device |
| PUT | `/api/devices/{id}/state` | Update device state |
| GET | `/api/rooms/{id}/devices` | Get devices in a room |

### Schedules

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/schedules` | Get all schedules |
| GET | `/api/schedules/{id}` | Get a schedule |
| POST | `/api/schedules` | Create a schedule |
| PUT | `/api/schedules/{id}` | Update a schedule |
| DELETE | `/api/schedules/{id}` | Delete a schedule |

---

## Example API Requests

### Create a room

```http
POST /api/rooms
```

```json
{
  "name": "Living Room"
}
```

Example response:

```json
{
  "id": 1,
  "name": "Living Room"
}
```

### Create a device

```http
POST /api/devices
```

```json
{
  "name": "Living Room Light",
  "type": "LIGHT",
  "roomId": 1
}
```

Example response:

```json
{
  "id": 1,
  "name": "Living Room Light",
  "type": "LIGHT",
  "roomId": 1,
  "enabled": false
}
```

### Change device state

```http
PUT /api/devices/1/state
```

```json
{
  "enabled": true
}
```

### Create a schedule

```http
POST /api/schedules
```

```json
{
  "deviceId": 1,
  "action": "TURN_ON",
  "time": "18:00"
}
```

Example response:

```json
{
  "id": 1,
  "deviceId": 1,
  "action": "TURN_ON",
  "time": "18:00:00"
}
```

---

## Validation and Error Handling

The API validates incoming requests.

Examples:

- Room names cannot be blank
- Device names cannot be blank
- Required IDs must be provided
- Required schedule fields must be provided
- Devices cannot be assigned to rooms that do not exist
- Schedules cannot be assigned to devices that do not exist

Common errors return appropriate HTTP status codes.

A global exception handler is used to provide consistent error responses.

---

## Testing

The project contains automated service-layer unit tests using:

- JUnit 5
- Mockito

Tests cover important application behaviour such as:

- Creating, retrieving, updating and deleting entities
- Validating relationships between entities
- Creating and updating schedules

To run the full test suite:

On Windows:

```bash
./mvnw.cmd test
```

On macOS/Linux:

```bash
.\mvnw test
```

---

## API Documentation

The API is documented using OpenAPI and Swagger UI.

After starting the application, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

The generated OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI allows the API endpoints to be explored and tested directly from a browser.

---

## Getting Started

### Prerequisites

Install:

- Java 21
- PostgreSQL
- Git

### Clone the repository

```bash
git clone https://github.com/mikekent72/unifiedhome-api.git
cd unifiedhome-api
```

### Configure PostgreSQL

Create a PostgreSQL database for the application.

Then configure the database connection in:

```text
src/main/resources/application.properties
```

Set the `DB_PASSWORD` environment variable to your local PostgreSQL password.

### Run the application

On Windows:

```bash
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

The API will start at:

```text
http://localhost:8080
```

---

## Project Structure

```text
unifiedhome-api/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/unifiedhome/api/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── UnifiedhomeApiApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│           └── com/unifiedhome/api/
│               ├── service/
│               └── UnifiedhomeApiApplicationTests.java
│
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## Future Improvements

Possible future improvements include:

- User accounts and authentication
- Role-based authorisation
- Background schedule execution
- Energy usage monitoring
- More advanced automation rules
- Docker-based deployment
- Integration testing
- Device discovery and hardware integration
- Physical smart-home hardware integration