FROM eclipse-temurin:17
WORKDIR /app
COPY reports-server/target/reports-server-*.jar reports-microservice.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "reports-microservice.jar"]
