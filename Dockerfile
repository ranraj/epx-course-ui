FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE $PORT
ADD target/epx-course-ui-0.0.1-SNAPSHOT.jar app.jar

RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]