FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/file-analysis-service-*.jar app.jar
VOLUME /uploads
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]