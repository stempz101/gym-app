FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
COPY src/ src/
RUN mvn -f pom.xml clean package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/discovery-microservice-*.jar discovery-microservice.jar
EXPOSE 8761:8761
ENTRYPOINT ["java", "-jar", "discovery-microservice.jar"]
