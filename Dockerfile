# Use GraalVM for building the native image
FROM ghcr.io/graalvm/graalvm-ce:latest as builder

# Install native-image tool
RUN gu install native-image

# Set the working directory
WORKDIR /app

# Copy the source code into the container
COPY . .

# Build the native image
RUN ./mvnw package -Pnative -DskipTests

# Create a new stage for the runtime image
FROM gcr.io/distroless/base-debian10

# Copy the native executable from the builder stage
COPY --from=builder /app/target/fms-api .

# Expose the port your app runs on
EXPOSE 8080

# Run the native executable
ENTRYPOINT ["./fms-api"]