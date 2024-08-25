# Start with a base image containing Java runtime
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file into the container
COPY target/rookies_talk-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java","-jar","app.jar"]
