# Use an official Java 21 runtime as a parent image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY cart-service/target/cart-service.jar app.jar

# Expose the port the application runs on
EXPOSE 80

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]