# Use GraalVM for building the native image
FROM ghcr.io/graalvm/graalvm-ce:latest as builder

# Install native-image tool
RUN gu install native-image

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/fms-api-0.0.1-SNAPSHOT.jar app.jar

# Build the native image from the JAR file
RUN native-image -jar app.jar

# Create a new stage for the runtime image
FROM alpine:latest

# Install necessary runtime dependencies
RUN apk add --no-cache libstdc++

# Copy the native executable from the builder stage
COPY --from=builder /app/app .

# Expose the port your app runs on
EXPOSE 8080

# Run the native executable
ENTRYPOINT ["./app"]