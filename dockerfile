# Use an OpenJDK base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR into the container
COPY build/libs/url-shortener-0.0.1-SNAPSHOT.jar app.jar

# Create directory for H2 file storage
RUN mkdir -p /data/h2

# Expose the port your app runs on (e.g., 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]