# Stage 1: Build the application
FROM amazoncorretto:21-alpine-jdk as BUILD

WORKDIR /app

RUN apk add maven

COPY pom.xml .
COPY .ebextensions .

RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

COPY src/main/resources/application.properties ./application.properties
COPY env.properties ./env.properties

COPY --from=BUILD /app/target/*.jar app.jar

EXPOSE 443

ENTRYPOINT ["java", "-jar", "app.jar"]
