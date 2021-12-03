FROM openjdk:8-alpine3.9
WORKDIR /app
ADD . /app/

USER root
RUN chmod +x gradlew

CMD ./gradlew clean test