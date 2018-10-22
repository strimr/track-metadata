FROM openjdk:11-jre-slim

RUN mkdir /app

WORKDIR /app

ADD ./api/target/track-metadata-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD java -jar track-metadata-api-1.0.0-SNAPSHOT.jar