FROM openjdk:17-oracle
COPY target/safe-guard-spring-0.0.1-SNAPSHOT.jar safe-guard-spring.jar
ENTRYPOINT ["java","-jar","safe-guard-spring.jar"]
