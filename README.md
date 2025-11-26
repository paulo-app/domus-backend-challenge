Domus Back-End Developer Challenge

Author: Paulo Cabello Acha
Tech Stack: Java 21 · Spring Boot 3 · WebFlux · WebClient · JUnit 5 · MockWebServer · Swagger/OpenAPI

🚀 Overview

This project implements the take-home coding challenge for the Domus Backend Developer role.

The goal is to expose an endpoint:

GET /api/directors?threshold=X


which returns all directors who have directed more movies than the given threshold, based on a remote paginated API:

https://challenge.iugolabs.com/api/movies/search?page=N


The returned list must be:

Strictly greater than threshold

Alphabetically sorted

Fail-proof (validation, errors, stability)

🧠 Architecture Summary and Decisions Taken:
✔ WebFlux + WebClient

Used for non-blocking, reactive HTTP calls to the external API.

✔ Intelligent Pagination

First request loads page 1 and extracts total_pages

Uses Flux.range(2, totalPages) to fetch only remaining pages

To be more efficient, it avoids redundant calls

Parallelized with CONCURRENCY = 5

✔ Clean Service Layer

All logic is contained within DirectorServiceImpl, adhering to the Single Responsibility Principle (SOLID).

✔ Validation

Negative threshold: empty list.

Non-numeric threshold: HTTP 400 with error message.

Threshold >= 0: service is executed normally.

✔ Swagger/OpenAPI

Automatically generates endpoint documentation.

✔ Testing Strategy

Unit tests with MockWebServer (fast and no external calls)

Integration tests with WebTestClient (controller-level)

This ensures correctness and prevents regressions.

📦 Project Structure:

src/main/java/com/domus/challenge
    ├── controller
    │     └── DirectorController.java
    ├── service
    │     ├── IDirectorService.java
    │     └── DirectorServiceImpl.java
    ├── dto
    │     ├── RemoteMovie.java
    │     └── RemotePageResponse.java
    └── config
          └── WebClientConfig.java

src/test/java/com/domus/challenge
    ├── service
    │     └── DirectorServiceTest.java
    └── controller
          └── DirectorControllerTest.java

🧩 Endpoint Documentation
GET /api/directors?threshold=X
Query Parameter:
Name	Type	Required	Description
threshold	integer	yes	Return directors with more than this number of movies
Example Response:
{
  "directors": [
    "Martin Scorsese",
    "Woody Allen"
  ]
}

Error Case:
{
  "error": ["threshold must be an integer"]
}

🛠 How to Run
1. Build
mvn clean install

2. Run
mvn spring-boot:run

App runs at:

http://localhost:8080

🧪 How to Run Tests
mvn test


Tests include:

- Reactive service pagination and mapping.

- Controller validation and responses.

- All API interactions fully mocked.

📘 Swagger UI

Once running, access:

http://localhost:8080/swagger-ui.html


or:

/swagger-ui/index.html

🧠 Technical Considerations & Decisions Taken

1) Reactive WebClient over RestTemplate

Required by challenge; fully non-blocking.

2) Avoid redundant calls

Page 1 is fetched once → remaining pages are fetched dynamically.

3) Graceful error handling

Threshold validation prevents incorrect calls.

4) Thread-safety & concurrency

Parallel fetches use a controlled concurrency level (=5).

5) DTO mapping with @JsonProperty

Ensures compatibility with remote JSON fields using uppercase keys.

6) MockWebServer for reliability

External API is never called during tests: fast, stable and predictable.

🏁 Challenge Completed

This solution is:

✔ Efficient
✔ Reactive
✔ Tested
✔ Documented
✔ Clean & maintainable
✔ Ready for production-level review

If you have any questions or want to discuss the design, I’ll be happy to walk you through it.
