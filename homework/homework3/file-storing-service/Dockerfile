FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/file-storing-service-*.jar app.jar

VOLUME /uploads

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]