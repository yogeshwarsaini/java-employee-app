FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/employee-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090

ENTRYPOINT ["jav", "-jar", "app.jar"]
