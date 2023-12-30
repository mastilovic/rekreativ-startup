# rekreativ

## About project

Project is created for keeping stats of football matches being played between teams. User is not part of team, teammates or matches entities, but is required for creating each of them.

## UML Diagram

![alt text](https://github.com/salexdxd/rekreativ-startup/blob/main/src/main/resources/static/UML/RekreativUML.drawio.png?raw=true)

## Description

User with admin role (ROLE_ADMIN) is initialized when project is ran. When registering, user gets a default user role (ROLE_USER). Every user can create team, teammates and matches played between teams. Teams are created without any teammates. Each team can have teammates that can be created by user. Teammates can be added to teams separately. Teams can play each other, which can be added in table Matches. For every match between teams - winner, scores and total games played will be kept for TeamA and TeamB. If match is deleted, scores and total games played of teams will be decreased accordingly.

Project has basic CRUD API for each of entities.

Docker image name of the application: `mastilovic/rekreativ:latest`

## Technology Stack
#### This project was built using IntelliJ IDEA and uses the following technologies:
* _Java 11_
* _Spring Framework/Spring Boot version 2.7.8_
* _Authentication and Authorization_
* _Auth0_
* _Gradle_
* _REST CRUD operations_
* _MySQL Docker Image 5.7_
* _Unit and Integration tests_
* _Rest Error Handlers_
* _Docker & Docker-compose_
* _Postman_
* _Git_
* _Installed IntelliJ IDEA plugin Sonarlint_

## Setup
### Setup for local development

1. Run Docker Desktop
2. Change active profile from `dev` to `local` in application.yaml
3. Build project in root of the project:
   * `gradle build`
4. Navigate to mysql folder in root of the project and run docker-compose.yaml:
    * `cd mysql`
    * `docker-compose up -d`
5. To run the application:
    * return to root of the project from mysql folder: `cd ..`
    * run application using gradle: `./gradlew bootRun`
6. Test the API using Postman
   * default user credentials:
     * username: `admin`
     * password: `admin`

### Setup for running application on other devices

1. Install Docker Desktop
2. Create profile on DockerHub
3. Install Postman for testing REST API
4. Clone GitHub repository:
   * `git clone https://github.com/salexdxd/rekreativ-startup.git`
5. Change directory to rekreativ-startup:
   * `cd rekreativ-startup`
6. Run docker compose:
    * `docker-compose up`
7. Wait for application to start
8. Test the API below using Postman


## API Reference

### Users

#### Register
```http request
POST /api/v1/users/register
```

| username: | admin |
|-----------|-------|
| password: | admin |

#### Register
```http request
POST /api/v1/users/login
```
##### Send raw json body:
{ <br />
    "username":"admin", <br />
    "password":"admin"
<br />}

##### Get token from headers
Click on headers, find header named token, copy string value

#### Send get request
Click on headers tab, as key enter `Authorization`, for value type `Bearer` ` ` `paste token value` 
```http request
GET /api/v1/users
```

## TODO: FRONTEND :)



