# Use an official Java 21 runtime as a parent image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
ARG JAR_FILE=target/messaging-service.jar
COPY ${JAR_FILE} app.jar

# Expose the port the application runs on
EXPOSE 8442

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]