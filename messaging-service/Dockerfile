# Use an official Java 21 runtime as a parent image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY messaging-service/target/messaging-service.jar app.jar

# Expose the port the application runs on
EXPOSE 8442

COPY paylate-microservices.p12 /app/paylate-microservices.p12
# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]