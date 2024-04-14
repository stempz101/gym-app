FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
COPY src/ src/
RUN mvn -f pom.xml clean package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/gym-app-*.jar gym-app.jar
EXPOSE 8080:8080
ENTRYPOINT ["java", "-jar", "gym-app.jar"]
