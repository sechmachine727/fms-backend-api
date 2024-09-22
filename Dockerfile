# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:17.0.12-alpine3.20

# Set the working directory
WORKDIR /home/sechmachine/actions-runner/fms/fms-backend-api/fms-backend-api

# Copy the jar file into the container
COPY target/fms-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
