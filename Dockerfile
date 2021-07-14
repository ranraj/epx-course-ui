FROM openjdk:15-jdk-alpine
RUN mvn install jar:jar \
    && cp -a target/epx-course-ui-0.0.1-SNAPSHOT.jar app.jar \
    && rm -rf target "$HOME/.m2"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]