FROM eclipse-temurin:17
WORKDIR /app
COPY target/discovery-microservice-*.jar discovery-microservice.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "discovery-microservice.jar"]
