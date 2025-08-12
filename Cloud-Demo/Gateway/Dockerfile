FROM openjdk:17-jdk-slim

WORKDIR /app
ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 7573

ENTRYPOINT ["java", "-jar", "app.jar"]
