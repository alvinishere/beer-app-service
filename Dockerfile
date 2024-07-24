## BASE IMAGE
#FROM openjdk:17
#
#WORKDIR /app
#COPY . /app
#RUN ./mvnw package
#
## COPY application build file into docker
#COPY target/*.jar app.jar
#
## Command to run application build file
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Stage 1 - build application with mvn
FROM openjdk:17 as builder
WORKDIR /project
ADD . /project
RUN ./mvnw package

# Stage 2 - move jar file into /app and execute with jdk-slim
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=builder /project/target/beerApp-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java", "-jar", "beerApp-0.0.1-SNAPSHOT.jar"]