# syntax=docker/dockerfile:1

FROM maven:3.8.3 AS maven

WORKDIR /app
COPY . /app
# Compile and package the application to an executable JAR

RUN mvn package


FROM openjdk:17-jdk-alpine3.14

WORKDIR /app

COPY --from=maven /app/target/app.jar /app/

CMD ["java","-jar","app.jar"]