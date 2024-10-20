FROM alpine:latest

# Install necessary libraries
RUN apk add --no-cache libstdc++ musl zlib

# Set the working directory
WORKDIR /app

# Copy the native executable into the container
COPY target/fms-api fms-api

# Copy the templates folder into the container
COPY src/main/resources/templates/Template_Import_Syllabus.xlsx /app/templates/Template_Import_Syllabus.xlsx

# Expose the port your app runs on
EXPOSE 8080

# Run the native executable
ENTRYPOINT ["./fms-api"]