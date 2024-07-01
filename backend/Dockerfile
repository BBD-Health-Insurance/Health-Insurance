FROM amazoncorretto:21-alpine-jdk AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add maven
RUN mvn clean package -DskipTests
WORKDIR /app
COPY ./target/backend-*.jar ./app.jar
EXPOSE 80
EXPOSE 443
ENTRYPOINT ["java","-jar","/app.jar"]