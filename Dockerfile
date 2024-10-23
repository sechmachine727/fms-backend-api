FROM container-registry.oracle.com/graalvm/jdk:23.0.1

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/fms-api-0.0.1-SNAPSHOT.jar app.jar

# Copy the templates folder into the container
COPY src/main/resources/templates /app

# Expose the port your app runs on
EXPOSE 8443

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
