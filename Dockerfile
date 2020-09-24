MAINTAINER https://github.com/jzarob
FROM openjdk:11-alpine
VOLUME /tmp
COPY target/docmm4j.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]