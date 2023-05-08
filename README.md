
# Vine Service

Spring boot application that provides an API for making operation with vine project:
- create vine
- get vines
- create order

## Configuration

1. login to Mysql server (change credentials in [application.yml](src/main/resources/application.yml))
2. create DB schema named **vine_db**

## Service Requirements

Requires:
- a running [Eureka registry](https://github.com/Volodymyr2907/vine-registration-service);
- a running RabbitMQ broker;
- a running Mysql DB;
- a running [user service](https://github.com/Volodymyr2907/user-service) (for interacting with permission)
- a running [messaging service](https://github.com/Volodymyr2907/notification-service) (for handling events)

## How to run

1. run [docker-compose](src/main/resources/docker/docker-compose.yml) file for MySql and RabbitMq 
2. ```bash mvn clean install```
3. run [Eureka registry](https://github.com/Volodymyr2907/vine-registration-service);
4. run [vine service](src/main/java/com/mentorship/vineservice/VineServiceApplication.java)
