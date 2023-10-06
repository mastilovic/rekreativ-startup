FROM openjdk:11

ARG JAR_FILE=build/libs/rekreativ-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]