FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY target ./target
RUN mvn clean package -DskipTests
FROM openjdk:11-jre-slim
WORKDIR /app
COPY /app/target/*.jar ./
EXPOSE 80
EXPOSE 443
CMD ["java", "-jar", "*.jar"]

# FROM amazoncorretto:21-alpine-jdk as BUILD
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# COPY target ./target
# RUN apk add maven
# RUN mvn clean package -DskipTests
# WORKDIR /app
# COPY /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
# EXPOSE 80
# EXPOSE 443
# ENTRYPOINT ["java","-jar","./target/backend-0.0.1-SNAPSHOT.jar"]