FROM gcr.io/distroless/base-debian11

# Set the working directory
WORKDIR /app

# Copy the native executable into the container
COPY target/fms-api .

# Copy the templates folder into the container
COPY src/main/resources/templates/Template_Import_Syllabus.xlsx /app/templates/Template_Import_Syllabus.xlsx

# Expose the port your app runs on
EXPOSE 8080

# Run the native executable
ENTRYPOINT ["./fms-api"]