# Use an official Gradle image with JDK 17 installed
FROM gradle:7.4.2-jdk17

# Set the working directory in the container
WORKDIR /app

# Copy the source code
COPY src src
COPY build.gradle .
COPY settings.gradle .

# Build the application
RUN gradle build -x test

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "build/libs/bancaya-1.0.0.jar"]