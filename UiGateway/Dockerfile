FROM eclipse-temurin:17-jdk-alpine
LABEL authors="marcodifiandra"
RUN apk update && apk add bash
RUN apk add --no-cache openssl ncurses-libs libstdc++

COPY target/UiGateway-0.0.1-SNAPSHOT.jar /app/uiGateway.jar

EXPOSE 8080
CMD ["java","-jar","uiGateway.jar"]
WORKDIR /app

