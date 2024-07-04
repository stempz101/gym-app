# Gym Application

## Navigation
- [Technologies](#Technologies)
- [How to run](#How-to-run)

# Technologies
- Java 17
- Spring (Boot, Web, Data, Cloud (Eureka, Resilience4J), Actuator, Security)
- OpenAPI 3
- JUnit 5
- Mockito
- Cucumber
- Testcontainers
- PostgreSQL
- MongoDB
- Redis
- Apache ActiveMQ
- Prometheus
- Docker

# How to run
## Locally

Before running the application, you should enable a Replica Set mode in MongoDB due to the use of transactions in `reports-microservice`. 

Link to configure Replica Set mode: https://www.mongodb.com/docs/manual/tutorial/convert-standalone-to-replica-set/ 

## Docker-Compose

Before running the application with **docker-compose**, you need to generate a secret key for MongoDB Replica Set. To do this, run the next script:
```bash
scripts/mongo-keyfile.sh
```

**!Warning!** Before executing docker-compose, be sure that all `.sh` files in "scripts" package have LF line endings.

After that, you will be able to run the application using the following command:
```bash
docker compose up -d
```
