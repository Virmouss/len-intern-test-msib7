# Use the official Gradle image as the base image
FROM gradle:7.6-jdk17 AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle /app/

# Download the dependencies
RUN gradle dependencies

# Copy the rest of the application code
COPY . /app/

# Build the application
RUN gradle build --no-daemon

# Use the official OpenJDK image as the base image for the runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/internLEN-0.0.1.jar /app/internLEN-0.0.1.jar

# Expose the ports for your application
EXPOSE 8080 9090

# Set the environment variables for PostgreSQL
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_NAME=internlen
ENV DB_USER=postgres
ENV DB_PASSWORD=android24

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/internLEN-0.0.1.jar"]
