FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar

RUN mkdir -p /opt/app

WORKDIR /opt/app

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]