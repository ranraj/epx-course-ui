FROM openjdk:11-jdk
ADD . /epx
WORKDIR /epx

VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=/epx/target/epx-course-ui-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]