FROM ibm-semeru-runtimes:open-17.0.12.1_7-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/fms-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
