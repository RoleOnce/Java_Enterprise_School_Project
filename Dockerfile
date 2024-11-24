# Use an official OpenJDK runtime as a parent image
FROM openjdk:23

# Set the working directory in the container
WORKDIR /app

ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container named /app and renames it to 'my-spring-app'
COPY target/Projektarbete_Web_Services-0.0.1-SNAPSHOT.jar /app/my-spring-boot-app.jar

# Expose the port that the application will run on (Must reflect Spring Boot's PORT)
EXPOSE 8443

# Command to run the app
ENTRYPOINT ["java", "-jar", "/app/my-spring-boot-app.jar"]

# Example build code : docker build -t my-spring-boot-app .
# Example run code : docker run -p 8443:8443 my-spring-boot-app