FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/ErrorsRepo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

LABEL authors="marcodifiandra"
ENTRYPOINT ["java", "-jar", "/app.jar"]