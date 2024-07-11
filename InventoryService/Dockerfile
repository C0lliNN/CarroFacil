#Build stage

FROM gradle:latest AS BUILD
WORKDIR /app
COPY . .
RUN gradle build -x test

# Package stage

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=BUILD /app/build/libs/*.jar /app/app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar"]


