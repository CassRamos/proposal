FROM amazoncorretto:21

COPY target/proposal-app-0.0.1-SNAPSHOT.jar proposal-app-0.0.1.jar

ENTRYPOINT ["java", "-jar", "proposal-app-0.0.1.jar"]
