FROM amazoncorretto:21-alpine
COPY rest/target/rest-0.0.1-SNAPSHOT.jar /auth.jar
ENTRYPOINT ["java", "-jar", "/auth.jar"]