FROM openjdk:21

WORKDIR /app

COPY ./target/ms_empleados-0.0.1-SNAPSHOT.jar .

EXPOSE 8600

ENTRYPOINT ["java", "-jar", "ms_empleados-0.0.1-SNAPSHOT.jar"]
