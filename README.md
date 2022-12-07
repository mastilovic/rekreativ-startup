# rekreativ-startup

## About project

Project is created for keeping stats of football matches being played between teams. User is not part of team, teammates or matches entities, but is required for creating each of them.

## UML Diagram

![alt text](https://github.com/salexdxd/rekreativ-startup/blob/main/src/main/resources/static/UML/RekreativUML.drawio.png?raw=true)

## Description

User with admin role (ROLE_ADMIN) is initialized when project is ran. When registering, user gets a default user role (ROLE_USER). Every user can create team, teammates and matches played between teams. Teams are created without any teammates. Each team can have teammates that can be created by user. Teammates can be added to teams separately. Teams can play each other, which can be added in table Matches. For every match between teams - winner, scores and total games played will be kept for TeamA and TeamB. If match is deleted, scores and total games played of teams will be decreased accordingly.

Project has basic CRUD API for each of entities.

## Authorization

Using JWT (JSON Web Token) for authorizing users. Dependency used for generating token is auth0. Roles (ROLE_USER, ROLE_ADMIN) are used for permitting users to access each endpoint. ROLE_USER can access any endpoint that has anything to do with Teammate, Team and Matches.

## Requirements to run the project

* Java 11
* Spring Boot Framework
* Tomcat apache
* Local database (MySQL, PostgreSQL or others)
* Configured database properties in project (src/main/resources/application.properties)

## TODO: FRONTEND :)



