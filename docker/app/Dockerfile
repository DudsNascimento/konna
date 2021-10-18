# syntax=docker/dockerfile:1
FROM alpine:latest
WORKDIR /java
ENV ENV_VERSION=0.0.1
RUN apk add openjdk11
COPY ./app/build/libs/app-${ENV_VERSION}.jar ./konna-${ENV_VERSION}.jar
EXPOSE 8080
ENTRYPOINT java -jar "konna-${ENV_VERSION}.jar"