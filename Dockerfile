# user_service/Dockerfile
FROM openjdk:17-alpine

WORKDIR /app

COPY target/notifcation_service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082
# Update to your service's port

CMD ["java", "-jar", "app.jar"]
