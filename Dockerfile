#FROM openjdk:17-oracle
FROM eclipse-temurin:21-jdk-ubi9-minimal
COPY target/safe-guard-spring-0.0.1-SNAPSHOT.jar safe-guard-spring.jar
ENTRYPOINT ["java","-jar","safe-guard-spring.jar"]
