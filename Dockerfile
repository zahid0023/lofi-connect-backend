# Use a base image with Java installed
FROM amazoncorretto:21

# Set a working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/lofi-connect-1.0.0.jar app.jar

# Expose the application's port
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
