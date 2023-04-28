FROM maven:3.8-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn -B -DskipTests clean package

FROM openjdk:17-jdk-alpine
COPY --from=build /app/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]