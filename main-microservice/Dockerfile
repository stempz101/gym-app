FROM eclipse-temurin:17
WORKDIR /app
COPY target/main-microservice-*.jar main-microservice.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "main-microservice.jar"]
