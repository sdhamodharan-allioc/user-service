#!/bin/bash
FROM gradle:jdk11 AS gradleimage

FROM --platform=linux/amd64 openjdk:11-jre-slim
ADD ./build/libs/user-service-1.0.0-SNAPSHOT.jar userservice.jar
ENTRYPOINT ["java","-jar","/userservice.jar"]