FROM openjdk:8-jdk
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=/home/travis/.m2/repository/com/cisco/epx/epx-course-ui/0.0.1-SNAPSHOT/epx-course-ui-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8787,suspend=n"
EXPOSE 8080 8787
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar /app.jar" ]