# Spring Retryable Redis Sessions

This project demonstrates how to implement a retry mechanism to handle Redis connection issues in a Spring Reactive application using Spring Session. The retry logic helps the application to be more robust and resilient against transient network failures, ensuring that user sessions are maintained and the user experience remains smooth even in the face of occasional connectivity issues.

## Prerequisites

- Java 17
- Gradle

## Dependencies

The following key dependencies are included in the `build.gradle` file:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'org.springframework.session:spring-session-data-redis'
    implementation 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'io.projectreactor:reactor-test'
    testImplementation 'org.springframework.security:spring-security-test'
}
```

These dependencies bring in Spring Webflux, Spring Security, Spring Data Redis (Reactive), Spring Session, Lombok.

## Project Structure

Here’s a high-level overview of the project's structure:

    Controller: Contains the SessionController that exposes a sample endpoint, demonstrating session creation, manipulation, and retrieval.
    Configuration:
        RedisSessionConfiguration: Contains beans related to Redis and session management, with the retry logic encapsulated within.
        WebfluxSecurityConfiguration: Handles the security aspects, ensuring endpoints are secured and provide logout functionality.
    Application: The entry point of our Spring Boot application is RedisSessionRetryApplication.

## Getting Started

### Build the project using Gradle:
```shell
./gradlew build

```


### Running the Application

Run the project using Gradle:

```shell
./gradlew bootRun

```

Navigate to http://localhost:8080 to interact with the application.

### Running Tests

TODO: add integration test with testcontainer

Execute the tests using Gradle:

```shell
./gradlew test

```
## Acknowledgments

    Spring Framework
    Spring Session
    Spring Data Redis
