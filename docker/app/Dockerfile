# syntax=docker/dockerfile:1
FROM alpine:latest
WORKDIR /java
ENV ENV_VERSION=${ENV_VERSION}
RUN apk add openjdk11
RUN apk add aws-cli
RUN aws codeartifact get-package-version-asset \
    --domain tiago-nascimento \
    --repo konna \
    --format maven \
    --package kotlin.konna:konna \
    --package-version $ENV_VERSION \
    konna-$ENV_VERSION.jar
RUN pip install -r requirements.txt
EXPOSE 8080
CMD java -jar konna-$ENV_VERSION.jar