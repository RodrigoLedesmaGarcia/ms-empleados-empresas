FROM openjdk:21

WORKDIR /app

COPY target/eureka-server-0.0.1-SNAPSHOT.jar .

EXPOSE 8761

ENTRYPOINT ["java", "-jar","eureka-server-0.0.1-SNAPSHOT.jar" ]
