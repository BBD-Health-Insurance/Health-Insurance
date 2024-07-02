FROM amazoncorretto:21-alpine-jdk as BUILD
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY target ./target
RUN apk add maven
RUN mvn clean install -DskipTests
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 80
EXPOSE 443
ENTRYPOINT ["java","-jar","app.jar"]