FROM eclipse-temurin:17-jdk-alpine
LABEL authors="marcodifiandra"
RUN apk update && apk add bash
RUN apk add --no-cache openssl ncurses-libs libstdc++

COPY target/ApiGateway-0.0.1-SNAPSHOT.jar /app/apiGateway.jar

EXPOSE 8080
CMD ["java","-jar","apiGateway.jar"]
WORKDIR /app

