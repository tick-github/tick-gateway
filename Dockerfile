FROM maven:3.8.6-openjdk-18-slim AS maven
COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn package

FROM openjdk:18-alpine
WORKDIR /app
ARG JAR_FILE=*.jar
COPY --from=maven target/${JAR_FILE} ./app/app.jar
CMD ["java", "-jar", "./app/app.jar"]