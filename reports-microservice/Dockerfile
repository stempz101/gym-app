FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
COPY src/ src/
RUN mvn -f pom.xml clean package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/reports-microservice-*.jar reports-microservice.jar
EXPOSE 8081:8081
ENTRYPOINT ["java", "-jar", "reports-microservice.jar"]
