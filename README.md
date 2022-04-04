# rekreativ-startup

About project

UML Diagram

![alt text](https://github.com/salexdxd/rekreativ-startup/blob/main/UML/RekreativUML.drawio.png?raw=true)

Verbal description

When registering, user gets a default user role (ROLE_USER). Every user can create a team. Each team can have teammates that can be created by user. Teams can play each other, which will be saved in table Matches. For every match between teams, score will be kept for TeamA and TeamB.

Project has basic CRUD API for each of entities.

Authorization

Using JWT (JSON Web Token) for authorizing users. Dependency used for generating token is auth0. Roles (ROLE_USER, ROLE_ADMIN) are used for permitting users to access each endpoint. ROLE_USER can access any endpoint that has anything to do with Teammate, Team and Matches.



