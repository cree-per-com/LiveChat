FROM openjdk:21-jdk

ARG JAR_FILE=/build/libs/LiveChat-0.0.1-SNAPSHOT.war

COPY ${JAR_FILE} app.jar

CMD {"java","-jar","app.jar"}