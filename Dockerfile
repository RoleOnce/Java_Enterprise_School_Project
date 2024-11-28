# Start från en OpenJDK-bild
FROM openjdk:17

# Sätt arbetskatalog
WORKDIR /app

# Kopiera JAR-filen från Maven byggkatalog (uppdaterat för att hantera eventuella namnvariationer)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/my-spring-boot-app.jar

# Exponera porten (samma som i applikationen)
EXPOSE 8443

# Starta applikationen
ENTRYPOINT ["java", "-jar", "/app/my-spring-boot-app.jar"]
